package com.marvellisimo

import android.app.Activity
import android.content.Intent
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import android.support.v7.widget.Toolbar
import android.view.View
import com.marvellisimo.character.CharacterActivity
import com.marvellisimo.favorite.FavoriteActivity
import com.marvellisimo.serie.SerieActivity
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem

object DrawerUtil {
    fun getDrawer(activity: Activity, toolbar: Toolbar) {

        val names = PrimaryDrawerItem().withIdentifier(0)
            .withName("Tobias Ask, John Gherga").withTextColorRes(R.color.nav_color_names)
            .withSelectable(false)

        val homeNav = PrimaryDrawerItem().withIdentifier(1)
            .withName(R.string.nav_home).withIcon(R.drawable.ic_home_black_24dp).withTextColorRes(R.color.textColorWhite)

        val charactersNav = PrimaryDrawerItem().withIdentifier(2)
            .withName(R.string.nav_characters).withIcon(R.drawable.ic_person_white_24dp).withTextColorRes(R.color.textColorWhite)

        val seriesNav = PrimaryDrawerItem()
            .withIdentifier(3).withName(R.string.nav_series).withIcon(R.drawable.ic_local_movies_black_24dp).withTextColorRes(R.color.textColorWhite)

        val favoritesNav = PrimaryDrawerItem().withIdentifier(4)
            .withName(R.string.nav_favorites).withIcon(R.drawable.ic_stars_black_24dp).withTextColorRes(R.color.textColorWhite)

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
                favoritesNav
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
                    return true
                }
            })
            .build()
    }
}