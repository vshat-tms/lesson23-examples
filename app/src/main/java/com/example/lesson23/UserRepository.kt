package com.example.lesson23

import com.github.javafaker.Faker
import com.google.gson.GsonBuilder
import java.io.File

object UserRepository {
    private var _users = mutableListOf<User>()
    var users: List<User> = _users
        private set

    private val gson = GsonBuilder().apply {
        setPrettyPrinting()
    }.create()

    private val faker = Faker()

    init {
        loadList()
    }

    fun addRandomUser() {
        _users.add(createRandomUser())
        saveList()
    }

    fun removeAll() {
        _users.clear()
        saveList()
    }

    fun removeLast() {
        if (_users.isEmpty()) return
        _users.removeAt(_users.lastIndex)
        saveList()
    }

    fun updateUser(index: Int, user: User) {
        _users[index] = user
        saveList()
    }

    private fun createRandomUser() = User(
        faker.name().firstName(),
        faker.name().lastName(),
        faker.address().fullAddress()
    )

    private fun loadList() {
        val file = File(MyApplication.instance.filesDir, "users.json")
        if (file.exists()) {
            file.bufferedReader().use {
                val userArray = gson.fromJson(it, Array<User>::class.java)
                _users = userArray.toMutableList()
                users = _users
            }
        }
    }

    private fun saveList() {
        val file = File(MyApplication.instance.filesDir, "users.json")
        file.bufferedWriter().use {
            gson.toJson(_users, it)
            users = _users
        }
    }
}