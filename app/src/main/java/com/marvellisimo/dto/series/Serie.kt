package com.marvellisimo.dto.series

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Serie (
    val id: Int,
    val title: String,
    val description: String?,
    val characters: SerieCharacter,
    val startYear: Int,
    val thumbnail: Thumbnail
): Parcelable