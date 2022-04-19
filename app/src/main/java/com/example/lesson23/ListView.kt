package com.example.lesson23

interface ListView {
    val supportsDisplayNewItem: Boolean

    fun displayList(users: List<User>)
    fun displayNewItemInList(user: User)
    fun askForWriteExternalStoragePermission()

}