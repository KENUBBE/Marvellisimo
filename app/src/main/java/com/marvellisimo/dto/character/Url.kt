package com.marvellisimo.dto.character

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Url(val type: String = "", val url: String = ""): Parcelable