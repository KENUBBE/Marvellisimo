package com.marvellisimo.user

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.marvellisimo.DrawerUtil
import com.marvellisimo.R
import kotlinx.android.synthetic.main.activity_online_user_list.*

class OnlineUserList : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private val onlineUsers = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_online_user_list)
        setSupportActionBar(toolbar_onlineUsers)
        DrawerUtil.getDrawer(this, toolbar_onlineUsers)
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        getOnlineUsers()
    }

    private fun renderUsers(onlineUsers: ArrayList<String>) {
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, onlineUsers)
        val listView: ListView = findViewById(R.id.usersList)
        adapter.notifyDataSetChanged()
        listView.adapter = adapter
    }

    private fun getOnlineUsers() {
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    addListener(document.data.getValue("id").toString())
                }
            }
    }

    private fun addListener(documentId: String) {
        db.collection("users").document(documentId)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot?.get("status") == "online") {
                    onlineUsers.add(snapshot.data?.getValue("email").toString())
                    renderUsers(onlineUsers)
                } else {
                    onlineUsers.remove(snapshot?.data?.getValue("email"))
                    renderUsers(onlineUsers)
                }
            }
    }
}
