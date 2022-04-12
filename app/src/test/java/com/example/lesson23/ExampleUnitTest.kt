package com.example.lesson23

import com.github.javafaker.Faker
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.serialization.Serializer
import kotlinx.serialization.json.Json
import org.json.JSONObject
import org.junit.Test

import org.junit.Assert.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    private val faker = Faker()

    val gson = GsonBuilder().apply {
        setPrettyPrinting()
    }.create()

    @Test
    fun testSomething() {
        val user = User(faker.name().firstName(), faker.name().lastName(), faker.address().fullAddress())

        val text = gson.toJson(user)
        println(text)

        val jsonText = """
            {
              "firstName": "Ivan",
              "lastName": "Petrov",
              "address": "9031 Noemi Extensions, South Charlieshire, IA 18859"
            }
        """.trimIndent()


        val deserializedUser = gson.fromJson(jsonText, User::class.java)

        println(deserializedUser.firstName)
        println(deserializedUser)



        val list = mutableListOf<User>()
        for(i in 1..10) {
            val item = User(faker.name().firstName(), faker.name().lastName(), faker.address().fullAddress())
            list.add(item)
        }

        val jsonListText= gson.toJson(list)
        println(jsonListText)

        val array = gson.fromJson(jsonListText, Array<User>::class.java)
        println(array.size)
        println(array.toList())
    }

}