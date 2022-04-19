package com.example.lesson23

data class User(
    val firstName: String,
    val lastName: String,
    val address: String
) {
    val id: Long = currentId++

    companion object {
        var currentId = 0L
    }

}