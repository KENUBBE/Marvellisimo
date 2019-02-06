package com.marvellisimo.service

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.marvellisimo.dto.series.Item

class ItemAdapter(private val mContext: Context, private val items: ArrayList<Item>) : BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Any? = null

    override fun getItemId(position: Int): Long = 0L

    // create a new ImageView for each item referenced by the Adapter
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val textView: TextView
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            textView = TextView(mContext)
            textView.setPadding(2, 2, 2, 2)
        } else {
            textView = convertView as TextView
        }

        textView.text = items[position].name
        return textView

    }


}