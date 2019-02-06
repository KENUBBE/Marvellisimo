package com.marvellisimo.character

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
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
import kotlinx.android.synthetic.main.activity_serie.*
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
        addTextWatcherOnSearchField()
    }

    override fun onPause() {
        super.onPause()
        character_searchField.clearFocus()
    }

    private fun addTextWatcherOnSearchField() {
        val sf: EditText = findViewById(R.id.character_searchField)
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
                renderCharacter(characters)
            } else {
                fetchCharacterByStartsWith(userInput)
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

    private fun fetchCharacterByStartsWith(userInput: Editable): Disposable {
        return createMarvelService()
            .getCharacterByStartWith(prefixApi + userInput + suffixApi + hashKEY)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { res -> println(res.data.results); searchResults.clear(); createSearchResult(res.data.results) },
                { error -> println("Error: ${error.message}") }
            )
    }

    private fun createSearchResult(result: ArrayList<Character>) {
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
                val intent = Intent(this, InfoActivity::class.java).apply {
                    action = Intent.ACTION_SEND
                    putExtra("char", characters[position])
                }

                //intent.putExtra("name", characters[position].name)
                //intent.putExtra("desc", characters[position].description)
                //intent.putExtra("thumbnail", characters[position].thumbnail.createUrl())
                startActivity(intent)
            }
    }

    private fun getOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        return builder.build()
    }
}
