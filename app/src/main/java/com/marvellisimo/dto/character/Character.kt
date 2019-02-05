package com.marvellisimo.dto.character

import android.os.Parcelable
import com.marvellisimo.dto.series.Item
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Character (
    var id: Int,
    var name: String,
    var description: String,
    var series: CharacterSerie,
    var thumbnail: Thumbnail
): Parcelable