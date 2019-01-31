package com.marvellisimo.dto.series

data class Thumbnail(val path: String, val extension: String) {
    fun createUrl(): String {
        return "$path.$extension"
    }
}