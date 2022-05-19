package com.example.lesson23.di

object ServiceLocatorExample {
    val serviceMap = mutableMapOf<Class<*>, Any>()

    init {
        single {
            DependencyFactories.createLoggingInterceptor()
        }

        addService(
            DependencyFactories.createHttpClient(get())
        )

        addService(
            DependencyFactories.createRetrofit(get())
        )

        addService(
            DependencyFactories.createUserApi(get())
        )
    }

    inline fun <reified T : Any> addService(instance: T) {
        serviceMap[T::class.java] = instance
    }

    fun <T> getService(clazz: Class<T>): T {
        return serviceMap[clazz] as T
    }

    inline fun <reified T> get(): T {
        return serviceMap[T::class.java] as T
    }

    inline fun <reified T : Any> single(lambda: () -> T) {
        serviceMap[T::class.java] = lambda()
    }
}