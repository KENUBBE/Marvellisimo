package com.marvellisimo

data class Todos(val userId: Int,
            val id: Int,
            val title: String,
            val completed: Boolean) {

    override fun toString(): String {
        return "\n Todos(userId=$userId, id=$id, title='$title', completed=$completed) \n"
    }
}

