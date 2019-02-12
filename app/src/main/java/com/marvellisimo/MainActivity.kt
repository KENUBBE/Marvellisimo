package com.marvellisimo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import com.marvellisimo.service.HexBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val marvelService = HexBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar_main)
        marvelService.generateHashKey()

        DrawerUtil.getDrawer(this, toolbar_main)

        val backgroundImageView: ImageView = findViewById(R.id.main_background)
        Picasso.get().load(R.drawable.marvel_main_background).fit().into(backgroundImageView)


    }

}
