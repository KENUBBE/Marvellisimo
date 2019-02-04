package com.marvellisimo.serie

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.marvellisimo.R
import com.marvellisimo.dto.series.Serie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_serie_info.*

class InfoActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {

    lateinit var serie: Serie
    lateinit var db: FirebaseFirestore
    lateinit var fav: CheckBox


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_serie_info)
        db = FirebaseFirestore.getInstance()
        serie = intent.getParcelableExtra("serie")
        fav = findViewById(R.id.fav_serie)
        fav.setOnCheckedChangeListener(this)

        infoName.text = serie.title
        if (serie.description != "" || serie.description != null) {
            infoDesc.text = serie.description
        } else infoDesc.text =
            getString(R.string.no_char_description)

        Picasso.get().load(serie.thumbnail.createUrl()).fit().centerCrop().into(infoThumbnail)

        isSerieInDB()
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (isChecked) {
            isSerieInDB()
        } else {
            deleteSerieFromFavorite()
        }
    }

    private fun addSerieToFavorite() {
        db.collection("favoriteSeries").document(serie.id.toString())
            .set(serie)
        Toast.makeText(this, "Added to favorites", Toast.LENGTH_LONG).show()
    }

    private fun deleteSerieFromFavorite() {
        db.collection("favoriteSeries").document(serie.id.toString())
            .delete()
        Toast.makeText(this, "Deleted from favorites", Toast.LENGTH_LONG).show()
    }

    private fun isSerieInDB() {
        db.collection("favoriteSeries").document(serie.id.toString())
            .get()
            .addOnSuccessListener { result ->
                if (result.exists()) {
                    fav.isChecked = true
                } else if (!result.exists() && fav.isChecked) {
                    addSerieToFavorite()
                }
            }
    }


}