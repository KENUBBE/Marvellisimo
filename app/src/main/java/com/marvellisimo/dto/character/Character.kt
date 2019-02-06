package com.marvellisimo.dto.character

import android.os.Parcelable
import com.marvellisimo.dto.series.Item
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Character(
    val id: Int = 0,
    val name: String = "",
    val description: String? = "",
    val series: CharacterSerie = CharacterSerie(arrayListOf(Item("", ""))),
    val thumbnail: Thumbnail = Thumbnail("", "")
) : Parcelable