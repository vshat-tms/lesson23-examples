package com.example.lesson23

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import java.lang.RuntimeException
import kotlin.concurrent.thread

sealed class ResultState<T> {
    class Loading<T> : ResultState<T>()
    class Error<T>(val e: Throwable) : ResultState<T>()
    class Success<T>(val data: T) : ResultState<T>()
}

fun example() {
    var ex: ResultState<Int>

    ex = ResultState.Loading()

    ex = ResultState.Success(23)

    ex = ResultState.Error(RuntimeException("example error"))


    when(ex) {
        is ResultState.Error -> println(ex.e.message)
        is ResultState.Loading -> println("loading")
        is ResultState.Success -> println("data: ${ex.data}")
    }
}

fun example2() {
    val x = exfn()

    val x2 = exfn().map {
        it * 2
    }


}

fun exfn(): LiveData<Int> {
    val x = MutableLiveData<Int>()

    AppExecutors.ioExecutor.execute {
        for(i in 1..5) {
            Thread.sleep(1000)
            x.postValue(i)
        }
    }
    return x
}