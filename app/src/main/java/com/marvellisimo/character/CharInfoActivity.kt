package com.marvellisimo.character

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore
import com.marvellisimo.DrawerUtil
import com.marvellisimo.R
import com.marvellisimo.dto.character.Character
import com.marvellisimo.dto.series.Serie
import com.marvellisimo.repository.MarvelService
import com.marvellisimo.serie.SerieInfoActivity
import com.marvellisimo.service.HexBuilder
import com.marvellisimo.service.ItemAdapter
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_char_info.*
import kotlinx.android.synthetic.main.activity_serie.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class CharInfoActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {

    lateinit var character: Character
    lateinit var db: FirebaseFirestore
    lateinit var fav: CheckBox
    private var serie: Serie = Serie()

    private val baseURL: String = "http://gateway.marvel.com/v1/public/series/"
    private val apiKEY: String = "?&ts=1&apikey=ca119f99531365ccb328f771ec231aa2&hash="
    private val hashKEY = HexBuilder().generateHashKey()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_char_info)
        db = FirebaseFirestore.getInstance()
        character = intent.getParcelableExtra("char")
        fav = findViewById(R.id.fav_char)
        fav.setOnCheckedChangeListener(this)

        infoName.text = character.name
        if (character.description == "" || character.description == null) {
            infoDesc.text = getString(R.string.no_char_description)
        } else {
            infoDesc.text = character.description
        }

        Picasso.get().load(character.thumbnail.createUrl()).fit().centerCrop().into(infoThumbnail)
        isCharacterInDB()
        renderSerie()

        setSupportActionBar(toolbar_charInfo)
        DrawerUtil.getDrawer(this, toolbar_charInfo)
    }

    private fun renderSerie() {
        val gridView: GridView = findViewById(R.id.char_serie_gridview)
        gridView.isVerticalScrollBarEnabled = false
        val adapter = ItemAdapter(this, character.series.items)
        gridView.adapter = adapter
        gridView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, v, position, id ->
                val serieId = character.series.items[position].resourceURI
                fetchSerieInfo(serieId)
                Handler().postDelayed({
                    val intent = Intent(this, SerieInfoActivity::class.java).apply {
                        action = Intent.ACTION_SEND
                        putExtra("serie", serie)
                    }
                    startActivity(intent)
                }, 1500)
            }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (isChecked) {
            isCharacterInDB()
        } else {
            deleteCharacterFromFavorite()
        }
    }

    private fun addCharacterToFavorite() {
        db.collection("favoriteCharacters").document(character.id.toString())
            .set(character)
        Toast.makeText(this, "Added to favorites", Toast.LENGTH_LONG).show()
    }

    private fun deleteCharacterFromFavorite() {
        db.collection("favoriteCharacters").document(character.id.toString())
            .delete()
        Toast.makeText(this, "Deleted from favorites", Toast.LENGTH_LONG).show()
    }

    private fun isCharacterInDB() {
        db.collection("favoriteCharacters").document(character.id.toString())
            .get()
            .addOnSuccessListener { result ->
                if (result.exists()) {
                    fav.isChecked = true
                } else if (!result.exists() && fav.isChecked) {
                    addCharacterToFavorite()
                }
            }
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

    private fun fetchSerieInfo(resourceUrl: String): Disposable {
        return createMarvelService()
            .getSerieByCharId(resourceUrl + apiKEY + hashKEY)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { res ->
                    serie = Serie(
                        res.data.results[0].id,
                        res.data.results[0].title,
                        res.data.results[0].description,
                        res.data.results[0].characters,
                        res.data.results[0].startYear,
                        res.data.results[0].thumbnail
                    )
                    println("SERIE FROM API $serie")
                },
                { error -> println("Error: ${error.message}") }
            )
    }

    private fun getOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        return builder.build()
    }
}
