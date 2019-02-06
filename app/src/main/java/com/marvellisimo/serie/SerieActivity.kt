package com.marvellisimo.serie

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.widget.AdapterView
import android.widget.EditText
import android.widget.GridView
import android.widget.Toast
import com.marvellisimo.R
import com.marvellisimo.dto.series.Serie
import com.marvellisimo.repository.Data
import com.marvellisimo.service.MarvelService
import com.marvellisimo.service.SerieImageAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class SerieActivity : AppCompatActivity() {

    private val baseURL: String = "http://gateway.marvel.com/v1/public/"
    private val apiKEY: String = "series?&ts=1&apikey=ca119f99531365ccb328f771ec231aa2&hash="
    private val prefixApi: String = "series?&titleStartsWith="
    private val suffixApi: String = "&ts=1&apikey=ca119f99531365ccb328f771ec231aa2&hash="
    private val hashKEY = MarvelService().generateHashKey()
    private val series = arrayListOf<Serie>()
    private var searchResults = arrayListOf<Serie>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchSerie()
        setContentView(R.layout.activity_serie)
        addTextWatcherOnSearchField()
    }

    private fun addTextWatcherOnSearchField() {
        val sf: EditText = findViewById(R.id.series_searchField)
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

    private fun createMarvelService(): Data {
        return Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(getOkHttpClient())
            .build()
            .create(Data::class.java)
    }

    private fun fetchSerie(): Disposable {
        return createMarvelService()
            .getSerieData(apiKEY + hashKEY)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { res -> createSerie(res.data.results) },
                { error -> println("Error: ${error.message}") }
            )
    }

    private fun fetchSerieByStartsWith(userInput: Editable): Disposable {
        return createMarvelService()
            .getSerieByStartWith(prefixApi + userInput + suffixApi + hashKEY)
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

    private fun renderSerie(series: ArrayList<Serie>) {
        val gridView: GridView = findViewById(R.id.serie_gridview)
        gridView.adapter = SerieImageAdapter(this, series)

        gridView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, v, position, id ->
                val intent = Intent(this, InfoActivity::class.java).apply {
                    action = Intent.ACTION_SEND
                    putExtra("serie", series[position])
                }
                /*intent.putExtra("title", series[position].title)
                intent.putExtra("desc", series[position].description)
                intent.putExtra("thumbnail", series[position].thumbnail.createUrl())*/
                startActivity(intent)
            }
    }

    private fun getOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        return builder.build()
    }
}
