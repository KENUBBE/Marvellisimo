package com.marvellisimo.dto.series

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SerieCharacter(val items: ArrayList<Item>) : Parcelable