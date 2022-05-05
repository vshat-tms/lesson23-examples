package com.example.lesson23.network

import com.example.lesson23.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
        var BASE_URL = "https://reqres.in/api/"

        fun create(): UserApi {
            val httpClient = OkHttpClient.Builder()

            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                httpClient.addInterceptor(logging)
            }

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .build()

            return retrofit.create(UserApi::class.java)
        }
    }
}