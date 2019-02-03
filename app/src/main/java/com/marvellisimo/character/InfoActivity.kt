package com.marvellisimo.character

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import android.widget.CompoundButton
import com.google.firebase.firestore.FirebaseFirestore
import com.marvellisimo.R
import com.marvellisimo.dto.character.Character
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_info.*

class InfoActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {

    lateinit var character: Character
    lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        db = FirebaseFirestore.getInstance()
        character = intent.getParcelableExtra("char")
        val fav: CheckBox = findViewById(R.id.fav_char)

        fav.setOnCheckedChangeListener(this)

        infoName.text = character.name
        if (character.description != "") infoDesc.text = character.description else infoDesc.text = getString(R.string.no_char_description)

        Picasso.get().load(character.thumbnail.createUrl()).fit().centerCrop().into(infoThumbnail)
    }


    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            if(isChecked) {
                
                // Add a new document with a generated ID
                /*db.collection("favoriteCharacters")
                    .add(character)
                    .addOnSuccessListener { documentReference ->
                        Log.d("favoriteCharacters", "DocumentSnapshot added with ID: " + documentReference.id)
                    }
                    .addOnFailureListener { e ->
                        Log.w("favoriteCharacters", "Error adding document", e)
                    }*/
            } else {

                println("Not added")
            }
    }
}
