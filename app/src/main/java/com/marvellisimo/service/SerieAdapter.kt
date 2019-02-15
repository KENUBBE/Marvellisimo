package com.marvellisimo.service

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.marvellisimo.R
import com.marvellisimo.character.CharacterActivity
import com.marvellisimo.dto.character.Character
import com.marvellisimo.dto.series.Serie
import com.marvellisimo.serie.SerieActivity
import com.squareup.picasso.Picasso

class SerieAdapter(context: Context, private val series: ArrayList<Serie>) : RecyclerView.Adapter<SerieAdapter.ViewHolder>() {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mClickListener: ItemClickListener? = null
    private val mContext = context

    // inflates the cell layout from xml when needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.recyclerview_item, parent, false)
        view.layoutParams = ViewGroup.LayoutParams(500, 500)
        return ViewHolder(view)
    }

    // binds the data to the TextView in each cell
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == series.size - 1) {
            (mContext as SerieActivity).loadNextResult(series.size)
        }

        holder.myTextView.scaleType = ImageView.ScaleType.CENTER_CROP
        Picasso.get().load(series[position].thumbnail.createUrl()).into(holder.myTextView)
    }

    // total number of cells
    override fun getItemCount(): Int {
        return series.size
    }


    // stores and recycles views as they are scrolled off screen
    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        internal var myTextView: ImageView = itemView.findViewById(R.id.info_text)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            if (mClickListener != null) mClickListener!!.onItemClick(view, adapterPosition)
        }
    }

    // allows clicks events to be caught
    internal fun setClickListener(itemClickListener: ItemClickListener) {
        this.mClickListener = itemClickListener
    }

    // parent activity will implement this method to respond to click events
    interface ItemClickListener {
        fun onItemClick(view: View, position: Int)
    }
}