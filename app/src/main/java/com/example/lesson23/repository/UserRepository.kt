package com.example.lesson23.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.lesson23.ResultState
import com.example.lesson23.db.User
import com.example.lesson23.db.UserDao
import com.github.javafaker.Faker
import java.util.concurrent.ExecutorService

class UserRepository(
    private val userDao: UserDao,
    private val ioExecutor: ExecutorService,
    private val faker: Faker
) {
    fun getAllUsers(userQuery: UserQuery): LiveData<List<User>> {
        val sortQueryText = when (userQuery.sortOrder) {
            UserSortOrder.NONE -> ""
            UserSortOrder.FIRST_NAME_ASC -> "ORDER BY first_name"
            UserSortOrder.FIRST_NAME_DESC -> "ORDER BY first_name DESC"
        }

        val params = mutableListOf<Any?>()
        val searchQueryText = if (userQuery.hasSearchQuery()) {
            "WHERE first_name LIKE '%' || ? || '%'"
        } else ""

        if (userQuery.hasSearchQuery()) {
            params.add(userQuery.searchQuery)
        }

        val queryText = "SELECT * FROM User $searchQueryText $sortQueryText"

        val query = SimpleSQLiteQuery(
            queryText,
            params.toTypedArray()
        )

        Log.d("UserRepository", "userQuery=$userQuery, queryText=$queryText")

        return userDao.getUsersByQuery(query)
    }

    fun getUserById(id: Long): LiveData<ResultState<User>> {
        val result = MutableLiveData<ResultState<User>>(ResultState.Loading())

        ioExecutor.execute {
            Thread.sleep(1000)

            val user = userDao.getUserById(id)
            if (user != null) {
                result.postValue(ResultState.Success(user))
            } else {
                result.postValue(ResultState.Error(RuntimeException("User not found")))
            }
        }

        return result
    }

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

    fun updateUser(user: User, onDoneCallback: () -> Unit) {
        ioExecutor.execute {
            Thread.sleep(1000)
            userDao.updateUser(user)
            onDoneCallback()
        }
    }

    private fun createRandomUser() = User(
        0,
        faker.name().firstName(),
        faker.name().lastName(),
        faker.address().fullAddress()
    )
}