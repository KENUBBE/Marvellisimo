package com.marvellisimo.dto.character

import android.os.Parcelable
import com.marvellisimo.dto.series.Item
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Character (
    var id: Int = 0,
    var name: String = "",
    var description: String = "",
    var series: CharacterSerie = CharacterSerie(arrayListOf(Item("",""))),
    var thumbnail: Thumbnail = Thumbnail("","")
): Parcelable