package com.marvellisimo.serie

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.widget.AdapterView
import android.widget.GridView
import com.marvellisimo.R
import com.marvellisimo.character.InfoActivity
import com.marvellisimo.dto.character.Character
import com.marvellisimo.dto.series.Serie
import com.marvellisimo.repository.Data
import com.marvellisimo.service.ItemAdapter
import com.marvellisimo.service.MarvelService
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_serie_info.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class InfoActivity : AppCompatActivity() {

    lateinit var serie: Serie
    lateinit var character: Character

    private val baseURL: String = "http://gateway.marvel.com/v1/public/characters/"
    private val apiKEY: String = "?&ts=1&apikey=ca119f99531365ccb328f771ec231aa2&hash="
    private val hashKEY = MarvelService().generateHashKey()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_serie_info)
        serie = intent.getParcelableExtra("serie")

        infoName.text = serie.title
        if (serie.description == "" || serie.description == null) {
            infoDesc.text = getString(R.string.no_char_description)
        } else infoDesc.text = serie.description

        Picasso.get().load(serie.thumbnail.createUrl()).fit().centerCrop().into(infoThumbnail)
        renderCharacter()
    }

    private fun renderCharacter() {
        val gridView: GridView = findViewById(R.id.serie_char_gridview)
        gridView.isVerticalScrollBarEnabled = false
        val adapter = ItemAdapter(this, serie.characters.items)
        gridView.adapter = adapter
        gridView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, v, position, id ->
                val resourceUrl = serie.characters.items[position].resourceURI
                fetchCharacterInfo(resourceUrl)
                Handler().postDelayed({
                    val intent = Intent(this, InfoActivity::class.java).apply {
                        action = Intent.ACTION_SEND
                        putExtra("char", character)
                    }
                    startActivity(intent)
                }, 1500)
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

    private fun fetchCharacterInfo(resourceUrl: String): Disposable {
        return createMarvelService()
            .getCharacterBySerieId("""$resourceUrl$apiKEY$hashKEY""")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { res ->
                    character = Character(
                        res.data.results[0].id,
                        res.data.results[0].name,
                        res.data.results[0].description,
                        res.data.results[0].series,
                        res.data.results[0].thumbnail
                    )
                    println("CHARACTER FROM API $character")
                },
                { error -> println("Error: ${error.message}") }
            )
    }

    private fun getOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        return builder.build()
    }


}