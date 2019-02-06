package com.marvellisimo.favorite

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore
import com.marvellisimo.R
import com.marvellisimo.dto.character.Character
import com.marvellisimo.dto.character.Thumbnail
import kotlinx.android.synthetic.main.activity_fav_char_list.*
import java.lang.StringBuilder

class FavCharList : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val favCharacters = arrayListOf<Character>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fav_char_list)
        loadFavoriteCharacter()
    }

    private fun loadFavoriteCharacter() {
        db.collection("favoriteCharacters")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val char = document.toObject(Character::class.java)
                    favCharacters.add(char)
                }
                println("CHARACTERS ${favCharacters.size}")
                favCharTv.text =
                    "${favCharacters[0].name} \n ${favCharacters[1].name} \n ${favCharacters[2].name} \n ${favCharacters[3].name}"
            }
    }

}
