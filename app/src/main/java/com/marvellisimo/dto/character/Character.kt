package com.marvellisimo.dto.character

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Character (
    val id: Int,
    val name: String,
    val description: String,
    val series: CharacterSerie,
    val thumbnail: Thumbnail
): Parcelable