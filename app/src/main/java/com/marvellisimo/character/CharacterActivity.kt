package com.marvellisimo.character

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import com.marvellisimo.DrawerUtil
import com.marvellisimo.R
import com.marvellisimo.dto.character.Character
import com.marvellisimo.repository.MarvelService
import com.marvellisimo.service.HexBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_character.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import com.marvellisimo.service.CharacterAdapter
import android.widget.Toast

class CharacterActivity : AppCompatActivity(), CharacterAdapter.ItemClickListener {
    private val baseURL: String = "http://gateway.marvel.com/v1/public/"
    private val apiKEY: String = "&apikey=ca119f99531365ccb328f771ec231aa2&hash="
    private val prefixApiOffset: String = "characters?&ts=1&offset="
    private val prefixApiSearch: String = "characters?&nameStartsWith="
    private val suffixApi: String = "&ts=1&apikey=ca119f99531365ccb328f771ec231aa2&hash="
    private val hashKEY = HexBuilder().generateHashKey()
    private var searchResults = arrayListOf<Character>()
    private val characters = arrayListOf<Character>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character)
        goBackToTop()
        fetchCharacter(characters.size)
        addTextWatcherOnSearchField()
        setSupportActionBar(toolbar_char)
        DrawerUtil.getDrawer(this, toolbar_char)
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

    private fun createMarvelService(): MarvelService {
        return Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(getOkHttpClient())
            .build()
            .create(MarvelService::class.java)
    }

    private fun fetchCharacter(offset: Int): Disposable {
        return createMarvelService()
            .getCharacterData(prefixApiOffset + offset + apiKEY + hashKEY)
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

    fun loadNextResult(offset: Int): Disposable {
        progressBarChar.bringToFront()
        progressBarChar.visibility = View.VISIBLE
        return createMarvelService()
            .getCharacterData(prefixApiOffset + offset + apiKEY + hashKEY)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { res ->
                    println("LOADING 20 MORE CHARACTERS"); createCharacter(res.data.results)
                },
                { error -> println("Error: ${error.message}") }
            )
    }

    private fun fetchCharacterByStartsWith(userInput: Editable): Disposable {
        return createMarvelService()
            .getCharacterByStartWith(prefixApiSearch + userInput + suffixApi + hashKEY)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { res ->
                    searchResults.clear()
                    createSearchResult(res.data.results)
                },
                { error -> println("Error: ${error.message}") }
            )
    }

    private fun createSearchResult(result: ArrayList<Character>) {
        for (res in result) {
            searchResults.add(res)
        }
        renderCharacter(searchResults)
        Toast.makeText(this, "Found ${searchResults.size} character(s)", Toast.LENGTH_LONG).show()
    }


    private fun renderCharacter(characters: ArrayList<Character>) {
        val recyclerView: RecyclerView = findViewById(R.id.char_recyclerview)
        val adapter = CharacterAdapter(this, characters)
        val numberOfColumns = 2
        val gridLayoutManager = GridLayoutManager(this, numberOfColumns)

        recyclerView.layoutManager = gridLayoutManager
        adapter.setClickListener(this)
        recyclerView.adapter = adapter
        recyclerView.scrollToPosition(characters.size - 20) // put next 20 characters on screen
        progressBarChar.visibility = View.GONE

    }

    override fun onItemClick(view: View, position: Int) {
        val intent = Intent(this, CharInfoActivity::class.java).apply {
            action = Intent.ACTION_SEND
            if (character_searchField.text.length > 3) {
                putExtra("char", searchResults[position])
            } else {
                putExtra("char", characters[position])
            }
        }
        startActivity(intent)
    }

    private fun goBackToTop() {
        val recyclerView: RecyclerView = findViewById(R.id.char_recyclerview)
        goTopChar_btn.setOnClickListener {
            recyclerView.smoothScrollToPosition(0)
        }
    }

    private fun getOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        return builder.build()
    }
}


