package com.example.lesson23.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lesson23.BuildConfig
import com.example.lesson23.PermissionChecker
import com.example.lesson23.db.AppDatabase
import com.example.lesson23.db.UserDao
import com.example.lesson23.network.UserApi
import com.example.lesson23.repository.NetworkUserRepository
import com.example.lesson23.repository.UserRepository
import com.github.javafaker.Faker
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object DependencyFactories {
    fun createUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    fun createAppDatabase(applicationContext: Context): AppDatabase {
        return Room
            .databaseBuilder(applicationContext, AppDatabase::class.java, "db")
            // чтобы база была в одном файлике (без  Write-Ahead-Log)
            .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
            .build()
    }

    fun createNetworkUserRepository(userApi: UserApi) = NetworkUserRepository(userApi)

    fun createUserRepository(userDao: UserDao, ioExecutor: Executor, faker: Faker) =
        UserRepository(userDao, ioExecutor, faker)

    fun createUserApi(retrofit: Retrofit) = retrofit.create(UserApi::class.java)

    fun createRetrofit(client: OkHttpClient) = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(UserApi.BASE_URL)
        .client(client)
        .build()

    fun createHttpClient(logger: HttpLoggingInterceptor): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            httpClient.addInterceptor(logger)
        }
        return httpClient.build()
    }

    fun createLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return logging
    }

    fun createIoExecutor(): ExecutorService {
        return Executors.newFixedThreadPool(4)
    }

    fun createPermissionChecker(applicationContext: Context): PermissionChecker {
        return PermissionChecker(applicationContext)
    }
}