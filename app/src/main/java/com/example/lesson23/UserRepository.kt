package com.example.lesson23

import com.github.javafaker.Faker
import com.google.gson.GsonBuilder
import java.io.File

object UserRepository {
    interface RepositoryChangeListener {
        fun onUserListUpdated(newList: List<User>)
    }

    private var _users = mutableListOf<User>()

    private val listeners = mutableListOf<RepositoryChangeListener>()

    private val gson = GsonBuilder().apply {
        setPrettyPrinting()
    }.create()

    private val faker = Faker()

    init {
        loadList()
    }

    fun addListener(listener: RepositoryChangeListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: RepositoryChangeListener) {
        listeners.remove(listener)
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

    fun updateUserById(id: Long, user: User, callback: () -> Unit) {
        AppExecutors.ioExecutor.execute {
            val index = _users.indexOfFirst { it.id == id }
            if (index != -1) {
                updateUser(index, user)
            }
            Thread.sleep(1000)
            callback()
        }
    }

    private fun createRandomUser() = User(
        faker.name().firstName(),
        faker.name().lastName(),
        faker.address().fullAddress()
    )

    private fun loadList() {
        AppExecutors.ioExecutor.execute {
            val file = File(MyApplication.instance.filesDir, "users.json")
            if (file.exists()) {
                file.bufferedReader().use {
                    val userArray = gson.fromJson(it, Array<User>::class.java)
                    _users = userArray.toMutableList()
                }
            }
            notifyListeners()
        }
    }

    private fun saveList() {
        AppExecutors.ioExecutor.execute {
            val file = File(MyApplication.instance.filesDir, "users.json")
            file.bufferedWriter().use {
                gson.toJson(_users, it)
            }
            Thread.sleep(1000)
            notifyListeners()
        }
    }

    private fun notifyListeners() {
        val listCopy = _users.toMutableList()
        listeners.forEach {
            it.onUserListUpdated(listCopy)
        }
    }

    fun getUserById(userId: Long, callback: (User?) -> Unit) {
        AppExecutors.ioExecutor.execute {
            Thread.sleep(1000)
            val result = _users.find { it.id == userId }
            callback(result)
        }
    }
}