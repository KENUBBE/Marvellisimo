package com.marvellisimo.favorite

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.marvellisimo.R
import com.squareup.picasso.Picasso

class FavoriteActivity : AppCompatActivity() {

    lateinit var charImg: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        navigateToFavoriteCharacter()
    }

    private fun navigateToFavoriteCharacter() {
        charImg = findViewById(R.id.fav_char_img)
        Picasso.get().load(R.drawable.marvel_characters).fit().centerCrop().into(charImg)
        charImg.setOnClickListener {
            val intent = Intent(this,FavCharList::class.java)
            startActivity(intent)
        }
    }
}
