package com.example.lesson23.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lesson23.db.AppDatabase
import com.example.lesson23.mappers.ReqresUserToUserMapper
import com.example.lesson23.screen.userlist.UserListViewModel
import com.github.javafaker.Faker
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val networkModule = module {
    single { DependencyFactories.createLoggingInterceptor() }
    single { DependencyFactories.createHttpClient(get()) }
    single { DependencyFactories.createRetrofit(get()) }
    single { DependencyFactories.createUserApi(get()) }
}

val dbModule = module {
    single {
        Room
            .databaseBuilder(get(), AppDatabase::class.java, "db")
            // чтобы база была в одном файлике (без  Write-Ahead-Log)
            .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
            .build()
    }
    single { get<AppDatabase>().userDao() }
}

val mainModule = module {
    single { Faker.instance() }
    single { DependencyFactories.createNetworkUserRepository(get()) }
    single { DependencyFactories.createUserRepository(get(), get(), get()) }
    single { ReqresUserToUserMapper() }
    single { DependencyFactories.createIoExecutor() }

    viewModel { UserListViewModel(get()) }
}

val appModules = listOf(
    networkModule,
    dbModule,
    mainModule
)