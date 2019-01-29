package com.marvellisimo.character

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import com.marvellisimo.repository.CharInterface
import com.marvellisimo.service.MarvelService
import com.marvellisimo.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_character.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class CharacterActivity : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance()
    val baseURL: String = "http://gateway.marvel.com/v1/public/"
    val apiKEY: String = "characters?&ts=1&apikey=ca119f99531365ccb328f771ec231aa2&hash="
    val hashKEY = MarvelService().generateHashKey()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character)
        //renderCharacter()
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

    fun fetchCharacter(view: View) {
        createMarvelService()
            .getCharacter(apiKEY + hashKEY)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { res -> println("RESULT $res") },
                { error -> println("Error: ${error.message}") }
            ).isDisposed
    }

    private fun getOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        return builder.build()
    }

    private fun renderCharacter() {
        val tvChar = TextView(this)
        charConstraint.addView(tvChar)
        tvChar.text = "HEhEEhEhehE LOL"
    }

}
