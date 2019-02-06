package com.marvellisimo.dto.character

import android.os.Parcelable
import com.marvellisimo.dto.series.Item
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CharacterSerie(val items: ArrayList<Item> = arrayListOf(Item("", ""))) : Parcelable