package com.marvellisimo.dto.series

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Item(val resourceURI: String, val name: String): Parcelable