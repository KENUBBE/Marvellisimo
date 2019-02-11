package com.marvellisimo

import android.app.Activity
import android.content.Intent
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem

import android.support.v7.widget.Toolbar
import android.view.View
import com.marvellisimo.character.CharacterActivity
import com.marvellisimo.favorite.FavoriteActivity
import com.marvellisimo.serie.SerieActivity

object DrawerUtil {
    fun getDrawer(activity: Activity, toolbar: Toolbar) {
        //if you want to update the items at a later time it is recommended to keep it in a variable

        val charactersNav = PrimaryDrawerItem().withIdentifier(1)
            .withName(R.string.nav_characters).withIcon(R.drawable.ic_person_white_24dp).withTextColorRes(R.color.textColorWhite)
        val seriesNav = PrimaryDrawerItem()
            .withIdentifier(2).withName(R.string.nav_series).withIcon(R.drawable.ic_local_movies_black_24dp).withTextColorRes(R.color.textColorWhite)


        val favoritesNav = SecondaryDrawerItem().withIdentifier(3)
            .withName(R.string.nav_favorites).withIcon(R.drawable.ic_stars_black_24dp).withTextColorRes(R.color.textColorWhite)



        //create the drawer and remember the `Drawer` result object
      DrawerBuilder()
            .withSliderBackgroundColorRes(R.color.colorPrimary)
            .withActivity(activity)
            .withToolbar(toolbar)
            .withActionBarDrawerToggle(true)
            .withActionBarDrawerToggleAnimated(true)
            .withCloseOnClick(true)
            .withSelectedItem(-1)
            .addDrawerItems(
                charactersNav,
                seriesNav,
                favoritesNav
            )
            .withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                override fun onItemClick(view: View, position: Int, drawerItem: IDrawerItem<*, *>): Boolean {
                    if (drawerItem.identifier == 1L && activity !is MainActivity) {
                        // load tournament screen
                        val intent = Intent(activity, CharacterActivity::class.java)
                        view.context.startActivity(intent)

                    }
                    if (drawerItem.identifier == 2L && activity !is MainActivity) {
                        // load tournament screen
                        val intent = Intent(activity, SerieActivity::class.java)
                        view.context.startActivity(intent)
                    }
                    if (drawerItem.identifier == 3L && activity !is MainActivity) {
                        // load tournament screen
                        val intent = Intent(activity, FavoriteActivity::class.java)
                        view.context.startActivity(intent)
                    }
                    return true
                }
            })
            .build()
    }
}