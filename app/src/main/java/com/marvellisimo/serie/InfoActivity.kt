package com.marvellisimo.serie

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.marvellisimo.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_info.*

class InfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        val bundle: Bundle = intent.extras!!
        val title = bundle.getString("title")
        val desc = bundle.getString("desc")
        val thumbnail = bundle.getString("thumbnail")

        infoName.text = title
        if (desc != null) infoDesc.text = desc else infoDesc.text = getString(R.string.no_serie_description)

        Picasso.get().load(thumbnail).fit().centerCrop().into(infoThumbnail)
    }
}