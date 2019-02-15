package com.marvellisimo

import android.app.Activity
import android.content.Intent
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import android.support.v7.widget.Toolbar
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.marvellisimo.character.CharacterActivity
import com.marvellisimo.favorite.FavoriteActivity
import com.marvellisimo.login.LoginActivity
import com.marvellisimo.serie.SerieActivity
import com.marvellisimo.user.OnlineUserList
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem

object DrawerUtil {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()


    fun getDrawer(activity: Activity, toolbar: Toolbar) {

        val names = PrimaryDrawerItem().withIdentifier(0)
            .withName(auth.currentUser?.email).withTextColorRes(R.color.nav_color_names)
            .withSelectable(false)

        val homeNav = PrimaryDrawerItem().withIdentifier(1)
            .withName(R.string.nav_home).withIcon(R.drawable.ic_home_black_24dp).withTextColorRes(R.color.textColorWhite)

        val charactersNav = PrimaryDrawerItem().withIdentifier(2)
            .withName(R.string.nav_characters).withIcon(R.drawable.ic_person_white_24dp).withTextColorRes(R.color.textColorWhite)

        val seriesNav = PrimaryDrawerItem()
            .withIdentifier(3).withName(R.string.nav_series).withIcon(R.drawable.ic_local_movies_black_24dp).withTextColorRes(R.color.textColorWhite)

        val favoritesNav = PrimaryDrawerItem().withIdentifier(4)
            .withName(R.string.nav_favorites).withIcon(R.drawable.ic_stars_black_24dp).withTextColorRes(R.color.textColorWhite)

        val usersOnline = PrimaryDrawerItem().withIdentifier(5)
            .withName("Users").withIcon(R.drawable.ic_users_24dp).withTextColorRes(R.color.textColorWhite)

        val signOut = PrimaryDrawerItem().withIdentifier(6)
            .withName("Sign out").withIcon(R.drawable.ic_sign_out_24dp).withTextColorRes(R.color.textColorWhite)

        val drawerHeader = AccountHeaderBuilder()
            .withActivity(activity)
            .withHeaderBackground(R.drawable.logodamarvelpng7)
            .build()

      DrawerBuilder()
          .withAccountHeader(drawerHeader)
            .withSliderBackgroundColorRes(R.color.colorPrimary)
            .withActivity(activity)
            .withToolbar(toolbar)
            .withActionBarDrawerToggle(true)
            .withActionBarDrawerToggleAnimated(true)
            .withCloseOnClick(true)
            .withSelectedItem(-1)
            .addDrawerItems(
                names,
                DividerDrawerItem(),
                homeNav,
                charactersNav,
                seriesNav,
                favoritesNav,
                usersOnline,
                signOut
            )
            .withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                override fun onItemClick(view: View, position: Int, drawerItem: IDrawerItem<*, *>): Boolean {
                    if (drawerItem.identifier == 1L) {
                        val intent = Intent(activity, MainActivity::class.java)
                        view.context.startActivity(intent)
                    }
                    if (drawerItem.identifier == 2L) {
                        val intent = Intent(activity, CharacterActivity::class.java)
                        view.context.startActivity(intent)
                    }
                    if (drawerItem.identifier == 3L) {
                        val intent = Intent(activity, SerieActivity::class.java)
                        view.context.startActivity(intent)
                    }
                    if (drawerItem.identifier == 4L) {
                        val intent = Intent(activity, FavoriteActivity::class.java)
                        view.context.startActivity(intent)
                    }
                    if (drawerItem.identifier == 5L) {
                        val intent = Intent(activity, OnlineUserList::class.java)
                        view.context.startActivity(intent)
                    }
                    if (drawerItem.identifier == 6L) {
                        val db = FirebaseFirestore.getInstance()
                        val userRef = db.collection("users").document(auth.currentUser?.uid.toString())
                        auth.signOut()
                        userRef
                            .update("status", "offline")

                        val intent = Intent(activity, LoginActivity::class.java)
                        view.context.startActivity(intent)
                    }
                    return true
                }
            })
            .build()
    }
}