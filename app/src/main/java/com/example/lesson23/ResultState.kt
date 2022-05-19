package com.example.lesson23

sealed class ResultState<T> {
    class Loading<T> : ResultState<T>()
    data class Error<T>(val throwable: Throwable) : ResultState<T>()
    data class Success<T>(val data: T) : ResultState<T>()
}
