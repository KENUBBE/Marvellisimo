package com.marvellisimo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.android.gms.tasks.OnCompleteListener



class Character : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character)
    }


    fun getFromDb(view: View) {
        // Read from the database
        db.collection("series")
            .get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d("name", document.id + " => " + document.data["name"])
                    }
                } else {
                    Log.w("name", "Error getting documents.", task.exception)
                }
            })
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
}
