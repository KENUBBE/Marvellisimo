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
    lateinit var serieImg: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        navigateToFavoriteCharacter()
        navigateToFavoriteSerie()
    }

    private fun navigateToFavoriteCharacter() {
        charImg = findViewById(R.id.fav_char_img)
        Picasso.get().load(R.drawable.marvel_characters).fit().centerCrop().into(charImg)
        charImg.setOnClickListener {
            val intent = Intent(this,FavCharList::class.java)
            startActivity(intent)
        }
    }

    private fun navigateToFavoriteSerie() {
        serieImg = findViewById(R.id.fav_series_img)
        Picasso.get().load(R.drawable.marvel_series).fit().centerCrop().into(serieImg)
        serieImg.setOnClickListener {
            val intent = Intent(this,FavSerieList::class.java)
            startActivity(intent)
        }
    }
}
