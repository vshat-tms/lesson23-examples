package com.example.lesson23.repository

import com.example.lesson23.db.User
import com.example.lesson23.db.UserDao
import com.github.javafaker.Faker
import java.util.concurrent.ExecutorService

class UserRepository(
    private val userDao: UserDao,
    private val ioExecutor: ExecutorService,
    private val faker: Faker
) {
    fun getAllUsers() = userDao.getAllUsers()

    fun getUserById(id: Long) = userDao.getUserById(id)

    fun deleteAll() {
        ioExecutor.execute {
            userDao.deleteAll()
        }
    }

    fun addRandomUser() {
        addUser(createRandomUser())
    }

    fun addUser(user: User) {
        ioExecutor.execute {
            userDao.insertUser(user)
        }
    }

    fun updateUser(user: User) = userDao.updateUser(user)

    private fun createRandomUser() = User(
        0,
        faker.name().firstName(),
        faker.name().lastName(),
        faker.address().fullAddress()
    )
}