package com.marvellisimo.serie

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.AdapterView
import android.widget.EditText
import android.widget.GridView
import com.marvellisimo.R
import com.marvellisimo.dto.series.Serie
import com.marvellisimo.repository.Data
import com.marvellisimo.service.MarvelService
import com.marvellisimo.service.SerieImageAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_serie.*
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
        var text: TextWatcher? = null

        text = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                isSearchFieldEmpty()
            }
            override fun afterTextChanged(editable: Editable) {}
        }
        sf.addTextChangedListener(text)
    }

    private fun isSearchFieldEmpty() {
        val tf = series_searchField.text.toString()
        if (tf.isBlank() || tf.length <= 1) {
            renderSerie(series)
        } else {
            fetchSerieByStartsWith()
        }
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
                {res -> createSerie(res.data.results)},
                {error -> println("Error: ${error.message}")}
            )
    }

    private fun fetchSerieByStartsWith(): Disposable {
        println(prefixApi + series_searchField.text.toString() + suffixApi + hashKEY)
        return createMarvelService()
            .getSerieByStartWith(prefixApi + series_searchField.text.toString() + suffixApi + hashKEY)
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
                    val intent = Intent(this, InfoActivity::class.java)
                    intent.putExtra("title", series[position].title)
                    intent.putExtra("desc", series[position].description)
                    intent.putExtra("thumbnail", series[position].thumbnail.createUrl())
                    startActivity(intent)
                }
    }

    private fun getOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        return builder.build()
    }
}
