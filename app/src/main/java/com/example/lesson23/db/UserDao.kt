package com.example.lesson23.db

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface UserDao {
    @RawQuery(observedEntities = [User::class])
    fun getUsersByQuery(query: SupportSQLiteQuery): LiveData<List<User>>

    @Query("SELECT * FROM user WHERE id=:id")
    fun getUserById(id: Long): User?

    @Query("DELETE FROM user")
    fun deleteAll()

    @Insert
    fun insertUser(user: User)

    @Update
    fun updateUser(user: User)
}