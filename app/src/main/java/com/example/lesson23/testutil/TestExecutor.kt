package com.example.lesson23.testutil

import java.util.concurrent.Executor

class TestExecutor : Executor {
    override fun execute(command: Runnable?) {
        command?.run()
    }
}