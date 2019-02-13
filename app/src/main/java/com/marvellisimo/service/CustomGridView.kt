package com.marvellisimo.service

import android.content.Context
import android.util.AttributeSet
import android.widget.AbsListView
import android.widget.GridView


class CustomGridView : GridView {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightSpec: Int

        if (layoutParams.height == AbsListView.LayoutParams.WRAP_CONTENT) {

            // The two leftmost bits in the height measure spec have
            // a special meaning, hence we can't use them to describe height.
            heightSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE shr 2, MeasureSpec.AT_MOST
            )
        } else {
            // Any other height should be respected as is.
            heightSpec = heightMeasureSpec
        }

        super.onMeasure(widthMeasureSpec, heightSpec)
    }
}