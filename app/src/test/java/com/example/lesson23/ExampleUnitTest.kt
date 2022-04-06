package com.example.lesson23

import com.github.javafaker.Faker
import org.junit.Test

import org.junit.Assert.*
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    private val faker = Faker()

    @Test
    fun testSomething() {
        val user2 = User(faker.name().firstName(), faker.name().lastName(), faker.address().fullAddress())
        for(i in 1..10) {
            println(User(faker.name().firstName(), faker.name().lastName(), faker.address().fullAddress()))
        }
    }

}