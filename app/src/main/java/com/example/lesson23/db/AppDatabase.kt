package com.example.lesson23.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        lateinit var instance: AppDatabase

        fun init(applicationContext: Context) {
            instance = Room
                .databaseBuilder(applicationContext, AppDatabase::class.java, "db")

                    // чтобы база была в одном файлике (без  Write-Ahead-Log)
                .setJournalMode(JournalMode.TRUNCATE)

                .build()
        }
    }
}