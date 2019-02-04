package com.marvellisimo.character

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.marvellisimo.R
import com.marvellisimo.dto.character.Character
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_info.*

class InfoActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {

    lateinit var character: Character
    lateinit var db: FirebaseFirestore
    lateinit var fav: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        db = FirebaseFirestore.getInstance()
        character = intent.getParcelableExtra("char")
        fav = findViewById(R.id.fav_char)
        fav.setOnCheckedChangeListener(this)

        infoName.text = character.name
        if (character.description != "") infoDesc.text = character.description else infoDesc.text = getString(R.string.no_char_description)

        Picasso.get().load(character.thumbnail.createUrl()).fit().centerCrop().into(infoThumbnail)
        isCharacterInDB()
    }


    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if(isChecked) {
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
                } else if (!result.exists() && fav.isChecked){
                    addCharacterToFavorite()
                }
            }
    }
}
