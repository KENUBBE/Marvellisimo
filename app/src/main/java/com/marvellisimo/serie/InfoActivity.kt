package com.marvellisimo.serie

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.marvellisimo.R
import com.marvellisimo.dto.series.Serie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_serie_info.*

class InfoActivity : AppCompatActivity() {

    lateinit var serie: Serie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_serie_info)
        serie = intent.getParcelableExtra("serie")

        infoName.text = serie.title
        if (serie.description != "") {
            infoDesc.text = serie.description
        } else infoDesc.text =
            getString(R.string.no_char_description)

        Picasso.get().load(serie.thumbnail.createUrl()).fit().centerCrop().into(infoThumbnail)
    }
}