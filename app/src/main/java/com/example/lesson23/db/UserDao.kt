package com.example.lesson23.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAllUsers(): LiveData<List<User>>

    @Query("SELECT * FROM user WHERE id=:id")
    fun getUserById(id: Long): LiveData<User?>

    @Query("DELETE FROM user")
    fun deleteAll()

    @Insert
    fun insertUser(user: User)

    @Update
    fun updateUser(user: User)
}