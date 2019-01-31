package com.marvellisimo.service

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.marvellisimo.dto.character.Character
import com.squareup.picasso.Picasso

class CharacterImageAdapter(private val mContext: Context, private val characters: ArrayList<Character>) : BaseAdapter() {

    override fun getCount(): Int = characters.size

    override fun getItem(position: Int): Any? = null

    override fun getItemId(position: Int): Long = 0L

    // create a new ImageView for each item referenced by the Adapter
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val imageView: ImageView
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = ImageView(mContext)
            imageView.layoutParams = ViewGroup.LayoutParams(500, 500)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView.setPadding(2, 2, 2, 2)
        } else {
            imageView = convertView as ImageView
        }

        Picasso.get().load(characters[position].thumbnail.createUrl()).fit().centerCrop().into(imageView)
        return imageView

    }


}