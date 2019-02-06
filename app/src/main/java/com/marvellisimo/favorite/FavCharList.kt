package com.marvellisimo.favorite

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore
import com.marvellisimo.R
import com.marvellisimo.dto.character.Character
import kotlinx.android.synthetic.main.activity_fav_char_list.*

class FavCharList : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val favCharacters = arrayListOf<Character>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fav_char_list)
    }
}
