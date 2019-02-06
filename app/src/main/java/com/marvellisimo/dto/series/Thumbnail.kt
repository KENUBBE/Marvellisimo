package com.marvellisimo.dto.series

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Thumbnail(val path: String = "", val extension: String = "") : Parcelable {
    fun createUrl(): String {
        return "$path.$extension"
    }
}