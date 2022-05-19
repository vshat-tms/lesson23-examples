package com.example.lesson23

import android.app.Application
import com.example.lesson23.di.DependencyStorage

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DependencyStorage.init(this)
    }
}