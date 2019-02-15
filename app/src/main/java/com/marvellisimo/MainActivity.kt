package com.marvellisimo

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ProcessLifecycleOwner
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.marvellisimo.service.HexBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), LifecycleObserver {
    private val marvelService = HexBuilder()
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        setSupportActionBar(toolbar_main)
        marvelService.generateHashKey()

        DrawerUtil.getDrawer(this, toolbar_main)

        val backgroundImageView: ImageView = findViewById(R.id.main_background)
        backgroundImageView.setPadding(0, 150, 0,0)
        Picasso.get().load(R.drawable.marvel_main_background).fit().into(backgroundImageView)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        //App in background
        setUserStatus(auth.currentUser, false)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        // App in foreground
        setUserStatus(auth.currentUser, true)
    }

    private fun setUserStatus(currentUser: FirebaseUser?, status: Boolean) {
        val userRef = db.collection("users").document(currentUser?.uid.toString())

        if (status) {
            userRef
                .update("status", "online")
        } else {
            userRef
                .update("status", "offline")
        }
    }
}
