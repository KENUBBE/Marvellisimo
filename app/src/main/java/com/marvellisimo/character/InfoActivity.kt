package com.marvellisimo.character

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.marvellisimo.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_info.*

class InfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        val bundle: Bundle = intent.extras!!
        val name = bundle.getString("name")
        val desc = bundle.getString("desc")
        val thumbnail = bundle.getString("thumbnail")

        infoName.text = name
        if (desc != "") infoDesc.text = desc else infoDesc.text = getString(R.string.info_description)

        Picasso.get().load(thumbnail).fit().centerCrop().into(infoThumbnail)
    }
}
