package com.marvellisimo.serie

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.GridView
import android.widget.Toast
import com.marvellisimo.DrawerUtil
import com.marvellisimo.R
import com.marvellisimo.character.CharInfoActivity
import com.marvellisimo.dto.character.Character
import com.marvellisimo.dto.series.Serie
import com.marvellisimo.repository.MarvelService
import com.marvellisimo.service.CharacterAdapter
import com.marvellisimo.service.HexBuilder
import com.marvellisimo.service.SerieAdapter
import com.marvellisimo.service.SerieImageAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_character.*
import kotlinx.android.synthetic.main.activity_serie.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class SerieActivity : AppCompatActivity(), SerieAdapter.ItemClickListener {

    private val baseURL: String = "http://gateway.marvel.com/v1/public/"
    private val apiKEY: String = "&apikey=ca119f99531365ccb328f771ec231aa2&hash="
    private val prefixApiSearch: String = "series?&titleStartsWith="
    private val prefixApiOffset: String = "series?&ts=1&offset="
    private val suffixApi: String = "&ts=1&apikey=ca119f99531365ccb328f771ec231aa2&hash="
    private val hashKEY = HexBuilder().generateHashKey()
    private val series = arrayListOf<Serie>()
    private var searchResults = arrayListOf<Serie>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_serie)
        fetchSerie(series.size)
        goToTop()
        addTextWatcherOnSearchField()
        setSupportActionBar(toolbar_serie)
        DrawerUtil.getDrawer(this, toolbar_serie)
    }

    override fun onPause() {
        super.onPause()
        serie_searchField.clearFocus()

    }

    private fun addTextWatcherOnSearchField() {
        val sf: EditText = findViewById(R.id.serie_searchField)
        val text: TextWatcher?

        text = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun afterTextChanged(editable: Editable) {
                isSearchFieldEmpty(editable)
            }
        }
        sf.addTextChangedListener(text)
    }

    private fun isSearchFieldEmpty(userInput: Editable) {
        Handler().postDelayed({
            if (userInput.length < 3) {
                renderSerie(series)
            } else {
                fetchSerieByStartsWith(userInput)
            }
        }, 700)
    }

    private fun createMarvelService(): MarvelService {
        return Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(getOkHttpClient())
            .build()
            .create(MarvelService::class.java)
    }

    private fun fetchSerie(offset: Int): Disposable {
        return createMarvelService()
            .getSerieData(prefixApiOffset + offset + apiKEY + hashKEY)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { res -> createSerie(res.data.results) },
                { error -> println("Error: ${error.message}") }
            )
    }

    private fun fetchSerieByStartsWith(userInput: Editable): Disposable {
        return createMarvelService()
            .getSerieByStartWith(prefixApiSearch + userInput + suffixApi + hashKEY)
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { res -> createSearchResult(res.data.results) },
                { error -> println("Error: ${error.message}") }
            )
    }

    private fun createSearchResult(result: ArrayList<Serie>) {
        searchResults.clear()
        for (res in result) {
            searchResults.add(res)
        }
        renderSerie(searchResults)
        Toast.makeText(this, "Found ${searchResults.size} serie(s)", Toast.LENGTH_LONG).show()
    }

    private fun createSerie(serie: ArrayList<Serie>) {
        for (ser in serie) {
            series.add(ser)
        }
        renderSerie(series)
    }

    fun loadNextResult(offset: Int): Disposable {
        progressBarSerie.bringToFront()
        progressBarSerie.visibility = View.VISIBLE
        return createMarvelService()
            .getSerieData(prefixApiOffset + offset + apiKEY + hashKEY)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { res ->
                    println("LOADING 20 MORE SERIES");
                    createSerie(res.data.results)
                },
                { error -> println("Error: ${error.message}") }
            )
    }

    private fun renderSerie(series: ArrayList<Serie>) {
        val adapter = SerieAdapter(this, series)
        val recyclerView: RecyclerView = findViewById(R.id.serie_recyclerview)
        val numberOfColumns = 2
        val gridLayoutManager = GridLayoutManager(this, numberOfColumns)

        recyclerView.layoutManager = gridLayoutManager
        adapter.setClickListener(this)
        recyclerView.adapter = adapter
        recyclerView.scrollToPosition(series.size - 20) // put next 20 characters on screen
        progressBarSerie.visibility = View.GONE
    }

    override fun onItemClick(view: View, position: Int) {
        val intent = Intent(this, SerieInfoActivity::class.java).apply {
            action = Intent.ACTION_SEND
            putExtra("serie", series[position])
        }
        startActivity(intent)
    }

    private fun goToTop() {
        val recyclerView: RecyclerView = findViewById(R.id.serie_recyclerview)
        goTopSerie_btn.setOnClickListener {
            recyclerView.smoothScrollToPosition(0)
        }
    }

    private fun getOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        return builder.build()
    }
}
