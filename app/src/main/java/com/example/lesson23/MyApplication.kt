package com.example.lesson23

import android.app.Application
import com.example.lesson23.di.DependencyStorage
import com.example.lesson23.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DependencyStorage.init(this)

        startKoin {
            androidContext(this@MyApplication)
            modules(appModules)
        }
    }
}