package com.example.lesson23.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.lesson23.ResultState
import com.example.lesson23.db.User
import com.example.lesson23.db.UserDao
import com.example.lesson23.repository.UserRepositoryTest.Companion.DEMO_USER_0
import com.example.lesson23.testutil.TestExecutor
import com.github.javafaker.Faker
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class UserRepositoryTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun getUserById_WhenUserIsPresent() {
        // GIVEN
        val dao = UserDaoStub()
        val sut = UserRepository(dao, TestExecutor(), Faker.instance())

        // WHEN
        val resultLiveData = sut.getUserById(1)

        // THEN
        assertEquals(ResultState.Success(DEMO_USER_0), resultLiveData.value)
    }

    @Test
    fun getUserById_WhenUserIsMissing() {
        // GIVEN
        val dao = UserDaoStub()
        val sut = UserRepository(dao, TestExecutor(), Faker.instance())

        // WHEN
        val resultLiveData = sut.getUserById(-1)

        // THEN
        assertTrue(resultLiveData.value is ResultState.Error)
    }

    @Test
    fun addUserCallsInsertUser() {
        // GIVEN
        val dao = UserDaoStub()
        val sut = UserRepository(dao, TestExecutor(), Faker.instance())

        // WHEN
        sut.addUser(DEMO_USER_0)

        // THEN
        assertTrue(dao.hasInsertUserCalled)
    }

    @Test
    fun addRandomUserCallsInsertUser() {
        // GIVEN
        val dao = UserDaoStub()
        val sut = UserRepository(dao, TestExecutor(), Faker.instance())

        // WHEN
        sut.addRandomUser()

        // THEN
        assertTrue(dao.hasInsertUserCalled)
    }

    @Test
    fun addRandomUsesFaker() {
        val user = User(
            0,
            "Random first",
            "Random last",
            "Random address"
        )

        val faker = Faker.instance()
        // faker.firstName() -> "Random first"


        // GIVEN
        val dao = UserDaoStub()
        val sut = UserRepository(dao, TestExecutor(), faker)

        // WHEN
        sut.addRandomUser()

        // THEN
        assertTrue(dao.hasInsertUserCalled)
    }

    companion object {
        val DEMO_USER_0 = User(
            0,
            "first",
            "last",
            "email"
        )
    }
}

class UserDaoStub : UserDao {
    var hasInsertUserCalled = false

    override fun getUsersByQuery(query: SupportSQLiteQuery): LiveData<List<User>> {
        TODO("Not yet implemented")
    }

    override fun getUserById(id: Long): User? {
        if (id < 0) return null
        return DEMO_USER_0
    }

    override fun deleteAll() {
        TODO("Not yet implemented")
    }

    override fun insertUser(user: User) {
        hasInsertUserCalled = true
    }

    override fun updateUser(user: User) {
        TODO("Not yet implemented")
    }
}