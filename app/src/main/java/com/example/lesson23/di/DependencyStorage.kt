package com.example.lesson23.di

import android.content.Context
import com.example.lesson23.PermissionChecker
import com.example.lesson23.db.AppDatabase
import com.example.lesson23.db.UserDao
import com.example.lesson23.mappers.ReqresUserToUserMapper
import com.example.lesson23.repository.UserRepository
import com.github.javafaker.Faker

object DependencyStorage {
    fun init(applicationContext: Context) {
        Android.init(applicationContext)
        Database.init()
        Dao.init()
        Repositories.init()
    }

    object Android {
        lateinit var applicationContext: Context
            private set

        lateinit var permissionChecker: PermissionChecker
            private set

        fun init(applicationContext: Context) {
            Android.applicationContext = applicationContext
            permissionChecker = DependencyFactories.createPermissionChecker(applicationContext)
        }
    }

    object Network {
        val loggingInterceptor = DependencyFactories.createLoggingInterceptor()
        val httpClient = DependencyFactories.createHttpClient(loggingInterceptor)
        val retrofit = DependencyFactories.createRetrofit(httpClient)
    }

    object Api {
        val userApi = DependencyFactories.createUserApi(Network.retrofit)
    }

    object Database {
        lateinit var appDatabase: AppDatabase
            private set

        fun init() {
            appDatabase = DependencyFactories.createAppDatabase(Android.applicationContext)
        }
    }

    object Dao {
        lateinit var userDao: UserDao
            private set

        fun init() {
            userDao = DependencyFactories.createUserDao(Database.appDatabase)
        }
    }

    object Repositories {
        val networkUserRepository = DependencyFactories.createNetworkUserRepository(Api.userApi)
        lateinit var userRepository: UserRepository
            private set

        fun init() {
            userRepository = DependencyFactories.createUserRepository(
                Dao.userDao,
                Executors.ioExecutor,
                Faker.instance()
            )
        }
    }

    object Mappers {
        val reqresUserMapper = ReqresUserToUserMapper()
    }

    object Executors {
        val ioExecutor = DependencyFactories.createIoExecutor()
    }
}