package com.marvellisimo.user

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.marvellisimo.R

class OnlineUserList : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private val onlineUsers = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_online_user_list)
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        getOnlineUsers()
    }

    private fun renderUsers(onlineUsers: ArrayList<String>) {
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, onlineUsers)
        val listView: ListView = findViewById(R.id.usersList)
        listView.adapter = adapter
    }

    private fun getOnlineUsers() {
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.get("status") == "online") {
                        onlineUsers.add(document.get("email").toString())
                        println(onlineUsers)
                    }
                }
                renderUsers(onlineUsers)
            }
    }
}
