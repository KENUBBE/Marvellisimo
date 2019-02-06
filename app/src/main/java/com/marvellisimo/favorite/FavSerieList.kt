package com.marvellisimo.favorite

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import com.google.firebase.firestore.FirebaseFirestore
import com.marvellisimo.R
import com.marvellisimo.dto.series.Serie
import com.marvellisimo.serie.SerieInfoActivity
import com.marvellisimo.service.SerieImageAdapter

class FavSerieList : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val series = arrayListOf<Serie>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fav_serie_list)
    }

    override fun onResume() {
        super.onResume()
        series.clear()
        loadFavoriteSerie()
    }

    private fun loadFavoriteSerie() {
        db.collection("favoriteSeries")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val serie = document.toObject(Serie::class.java)
                    series.add(serie)
                }
                renderSerie(series)
            }
    }

    private fun renderSerie(series: ArrayList<Serie>) {
        val gridView: GridView = findViewById(R.id.fav_serie_list)
        gridView.adapter = SerieImageAdapter(this, series)
        gridView.onItemClickListener =
            OnItemClickListener { parent, v, position, id ->
                val intent = Intent(this, SerieInfoActivity::class.java).apply {
                    action = Intent.ACTION_SEND
                    putExtra("serie", series[position])
                }
                startActivity(intent)
            }
    }
}
