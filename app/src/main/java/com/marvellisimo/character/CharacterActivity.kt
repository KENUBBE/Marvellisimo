package com.marvellisimo.character

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore
import com.marvellisimo.repository.Data
import com.marvellisimo.service.MarvelService
import com.marvellisimo.R
import com.marvellisimo.dto.character.Character
import com.marvellisimo.service.CharacterImageAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_character.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class CharacterActivity : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance()
    private val baseURL: String = "http://gateway.marvel.com/v1/public/"
    private val apiKEY: String = "characters?&ts=1&apikey=ca119f99531365ccb328f771ec231aa2&hash="
    private val prefixApi: String = "characters?&nameStartsWith="
    private val suffixApi: String = "&ts=1&apikey=ca119f99531365ccb328f771ec231aa2&hash="
    private val hashKEY = MarvelService().generateHashKey()
    private val characters = arrayListOf<Character>()
    private var searchResults = arrayListOf<Character>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchCharacter()
        setContentView(R.layout.activity_character)


        val sf: EditText = findViewById(R.id.searchField)




        var text: TextWatcher? = null


        text = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                isSearchFieldEmpty()
            }

            override fun afterTextChanged(editable: Editable) {

            }
        }

        sf.addTextChangedListener(text)


    }



    private fun isSearchFieldEmpty() {
        val tf = searchField.text.toString()
        if (tf.isBlank() || tf.length <= 1) {
            renderCharacter(characters)
        } else {
            fetchCharacterByStartsWith()
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

    protected fun fetchCharacterByStartsWith(): Disposable {
        println(prefixApi + searchField.text.toString() + suffixApi + hashKEY)
        return createMarvelService()
            .getCharacterByStartWith(prefixApi + searchField.text.toString() + suffixApi + hashKEY)
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { res -> createSearchResult(res.data.results) },
                { error -> println("Error: ${error.message}") }
            )
    }

    private fun createSearchResult(result: ArrayList<Character>) {
        searchResults.clear()
        for (res in result) {
            searchResults.add(res)
        }
        renderCharacter(searchResults)
    }

    private fun createCharacter(character: ArrayList<Character>) {
        for (char in character) {
            characters.add(char)
        }
        renderCharacter(characters)
    }

    private fun renderCharacter(characters: ArrayList<Character>) {
        val gridView: GridView = findViewById(R.id.char_gridview)
        gridView.adapter = CharacterImageAdapter(this, characters)

        gridView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, v, position, id ->
                val intent = Intent(this, InfoActivity::class.java)
                intent.putExtra("name", characters[position].name)
                intent.putExtra("desc", characters[position].description)
                intent.putExtra("thumbnail", characters[position].thumbnail.createUrl())
                startActivity(intent)
            }
    }

    private fun getOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        return builder.build()
    }
}
