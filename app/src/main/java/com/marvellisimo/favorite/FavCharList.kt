package com.marvellisimo.favorite


import kotlinx.android.synthetic.main.activity_fav_char_list.*
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.GridView
import com.google.firebase.firestore.FirebaseFirestore
import com.marvellisimo.R
import com.marvellisimo.character.InfoActivity
import com.marvellisimo.dto.character.Character
import com.marvellisimo.service.CharacterImageAdapter

class FavCharList : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val characters = arrayListOf<Character>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fav_char_list)
    }

    override fun onResume() {
        super.onResume()
        characters.clear()
        loadFavoriteCharacter()
    }

    private fun loadFavoriteCharacter() {
        db.collection("favoriteCharacters")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val char = document.toObject(Character::class.java)
                    characters.add(char)
                }
                renderCharacter(characters)
            }
    }

    private fun renderCharacter(characters: ArrayList<Character>) {
        val gridView: GridView = findViewById(R.id.fav_char_list)
        gridView.adapter = CharacterImageAdapter(this, characters)

        gridView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, v, position, id ->
                val intent = Intent(this, InfoActivity::class.java).apply {
                    action = Intent.ACTION_SEND
                    putExtra("char", characters[position])
                }
                startActivity(intent)
            }
    }
}
