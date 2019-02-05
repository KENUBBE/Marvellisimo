package com.marvellisimo.favorite

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore
import com.marvellisimo.R
import com.marvellisimo.dto.character.Character

class FavCharList : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val characters = arrayListOf<Character>()


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

                    val asd = document.data.values

                    /*val char = Character(
                        Integer.parseInt(document.get("id").toString()),
                        document.get("name").toString(),
                        document.get("description").toString(),
                        document.data.getValue("series"),
                        document.get("thumbnail") as Thumbnail
                    )*/
                }
                println("CHARACTERS $characters")
            }
    }
}
