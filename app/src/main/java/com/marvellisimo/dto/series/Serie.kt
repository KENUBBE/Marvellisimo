package com.marvellisimo.dto.series

data class Serie (
    val id: Int,
    val title: String,
    val description: String,
    val startYear: Int,
    val thumbnail: Thumbnail
)