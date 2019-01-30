package com.marvellisimo.dto

data class Character (
    val id: Int,
    val name: String,
    val description: String,
    val series: Any,
    val thumbnail: Thumbnail
)