package com.example.lesson23.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path


interface UserApi {
    @GET("users?delay=2")
    fun getUsers(): Call<ReqresUserListApiResponse>

    @GET("users/{id}?delay=2")
    fun getUserById(@Path("id") id: Long): Call<ReqresUserApiResponse>

    @PUT("users/{id}?delay=2")
    fun updateUserById(@Path("id") id: Long, @Body user: ReqresUser): Call<ResponseBody>

    companion object {
        const val BASE_URL = "https://reqres.in/api/"
    }
}