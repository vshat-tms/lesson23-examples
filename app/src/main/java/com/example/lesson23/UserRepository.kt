package com.example.lesson23

import com.github.javafaker.Faker

object UserRepository {
    private val _users = mutableListOf<User>()
    val users: List<User> = _users

    private val faker = Faker()

    init {
        repeat(10) {
            addRandomUser()
        }
    }

    fun addRandomUser() {
        _users.add(createRandomUser())
    }

    fun removeAll() {
        _users.clear()
    }

    fun removeLast() {
        if(_users.isEmpty()) return
        _users.removeAt(_users.lastIndex)
    }

    fun updateUser(index: Int, user: User) {
        _users[index] = user
    }

    private fun createRandomUser() = User(
        faker.name().firstName(),
        faker.name().lastName(),
        faker.address().fullAddress()
    )
}