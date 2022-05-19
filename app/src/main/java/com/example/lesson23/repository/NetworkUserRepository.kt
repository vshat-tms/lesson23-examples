package com.example.lesson23.repository

import com.example.lesson23.di.ServiceLocatorExample
import com.example.lesson23.network.ReqresUser
import com.example.lesson23.network.ReqresUserApiResponse
import com.example.lesson23.network.ReqresUserListApiResponse
import com.example.lesson23.network.UserApi
import okhttp3.ResponseBody
import retrofit2.Callback

class NetworkUserRepository(private val userApi: UserApi) {
    fun getUsers(callback: Callback<ReqresUserListApiResponse>) {
        val myUserApi = ServiceLocatorExample.getService(UserApi::class.java)
        myUserApi.getUsers().enqueue(callback)
    }

    fun getUsersById(id: Long, callback: Callback<ReqresUserApiResponse>) {
        userApi.getUserById(id).enqueue(callback)
    }

    fun updateUserById(user: ReqresUser, callback: Callback<ResponseBody>) {
        userApi.updateUserById(user.id, user).enqueue(callback)
    }

}