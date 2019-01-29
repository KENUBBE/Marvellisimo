package com.marvellisimo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class Character : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character)
    }


    fun getFromDb(view: View) {
        // Read from the database
        db.collection("Series")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d("name", document.id + " => " + document.data["name"])
                    }
                } else {
                    Log.w("name", "Error getting documents.", task.exception)
                }
            }
    }

    fun addToDb(view: View) {
        // Create a new user with a first and last name
        val data = HashMap<String, Any>()
        data["name"] = "Spiderman"

        db.collection("Series")
            .add(data)
            .addOnSuccessListener { documentReference ->
                Log.d("name", "DocumentSnapshot written with ID: " + documentReference.id)
            }
            .addOnFailureListener { e ->
                Log.w("name", "Error adding document", e)
            }
    }



    private fun getMarvelService(): MarvelService {
        return Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(getOkHttpClient())
            .build()
            .create(MarvelService::class.java)
    }

    fun getUserInfo(view: View) {
        getMarvelService()
            .getData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({res -> println("RESULT $res")},
                { error -> println("Error: ${error.message}")}).dispose()
    }

    private fun getOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        return builder.build()
    }

}
