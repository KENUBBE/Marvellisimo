package com.marvellisimo.dto.series

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Serie(
    val id: Int = 0,
    val title: String = "",
    val description: String? = "",
    val characters: SerieCharacter = SerieCharacter(arrayListOf(Item("", ""))),
    val startYear: Int = 0,
    val thumbnail: Thumbnail = Thumbnail("", "")
) : Parcelable