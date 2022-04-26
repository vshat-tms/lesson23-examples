package com.example.lesson23

import java.util.concurrent.Executors

object AppExecutors {
    val ioExecutor = Executors.newFixedThreadPool(4)
}