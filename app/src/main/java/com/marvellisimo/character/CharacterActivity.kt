package com.marvellisimo.character

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore
import com.marvellisimo.repository.CharInterface
import com.marvellisimo.service.MarvelService
import com.marvellisimo.R
import com.marvellisimo.dto.Character
import com.marvellisimo.service.ImageAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class CharacterActivity : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance()
    private val baseURL: String = "http://gateway.marvel.com/v1/public/"
    private val apiKEY: String = "characters?&ts=1&apikey=ca119f99531365ccb328f771ec231aa2&hash="
    private val hashKEY = MarvelService().generateHashKey()
    private val characters = arrayListOf<Character>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchCharacter()
        setContentView(R.layout.activity_character)

    }

    private fun createMarvelService(): CharInterface {
        return Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(getOkHttpClient())
            .build()
            .create(CharInterface::class.java)
    }

    private fun fetchCharacter(): Disposable {
        return createMarvelService()
            .getCharacterData(apiKEY + hashKEY)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { res -> createCharacter(res.data.results) },
                { error -> println("Error: ${error.message}") }
            )
    }

    private fun createCharacter(character: ArrayList<Character>) {
        for (char in character) {
            characters.add(char)
        }
        renderCharacter(characters)
    }

    private fun renderCharacter(characters: ArrayList<Character>) {
        val gridview: GridView = findViewById(R.id.gridview)
        gridview.adapter = ImageAdapter(this, characters)

        gridview.onItemClickListener =
                AdapterView.OnItemClickListener { parent, v, position, id ->
                    Toast.makeText(this, "$position", Toast.LENGTH_SHORT).show()
                }
    }

    private fun getOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        return builder.build()
    }
}
