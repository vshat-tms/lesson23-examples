package com.example.lesson23.di

import com.example.lesson23.network.UserApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object ServiceLocatorExample {
    private val serviceMap = mutableMapOf<Class<*>, Any>()

    init {
        addService(
            HttpLoggingInterceptor::class.java,
            DependencyFactories.createLoggingInterceptor()
        )

        addService(
            OkHttpClient::class.java,
            DependencyFactories.createHttpClient(getService(HttpLoggingInterceptor::class.java))
        )

        addService(
            Retrofit::class.java,
            DependencyFactories.createRetrofit(getService(OkHttpClient::class.java))
        )

        addService(
            UserApi::class.java,
            DependencyFactories.createUserApi(getService(Retrofit::class.java))
        )
    }

    fun <T : Any> addService(clazz: Class<T>, instance: T) {
        serviceMap[clazz] = instance
    }

    fun <T> getService(clazz: Class<T>): T {
        return serviceMap[clazz] as T
    }
}