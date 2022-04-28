package com.example.lesson23

import android.app.Application
import com.example.lesson23.db.AppDatabase
import com.example.lesson23.repository.UserRepository

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        AppDatabase.init(this)
    }

    companion object {
        lateinit var instance: MyApplication
    }
}