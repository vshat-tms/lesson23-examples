package com.example.lesson23.screen.networkuserdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lesson23.ResultState
import com.example.lesson23.db.User
import com.example.lesson23.mappers.ReqresUserToUserMapper
import com.example.lesson23.network.InvalidResponseException
import com.example.lesson23.network.ReqresUserApiResponse
import com.example.lesson23.network.UserNotFoundException
import com.example.lesson23.repository.NetworkUserRepository
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NetworkUserDetailsViewModel(
    private val userId: Long,
    private val networkUserRepository: NetworkUserRepository,
    private val userMapper: ReqresUserToUserMapper
) : ViewModel() {
    private var userCache: User? = null

    private val _user = MutableLiveData<ResultState<User>>()
    val user: LiveData<ResultState<User>> = _user

    private val _needCloseScreen = MutableLiveData<Boolean>()
    val needCloseScreen: LiveData<Boolean> = _needCloseScreen

    private val _isSavingUser = MutableLiveData<Boolean>()
    val isSavingUser: LiveData<Boolean> = _isSavingUser

    init {
        loadUser()
    }

    fun loadUser() {
        _user.value = ResultState.Loading()

        networkUserRepository.getUsersById(userId, object : Callback<ReqresUserApiResponse> {
            override fun onResponse(
                call: Call<ReqresUserApiResponse>,
                response: Response<ReqresUserApiResponse>
            ) {
                if (!response.isSuccessful) {
                    _user.value = ResultState.Error(UserNotFoundException())
                    return
                }

                val responseBody = response.body()

                if (responseBody == null) {
                    _user.value = ResultState.Error(InvalidResponseException())
                    return
                }

                val user = userMapper.toUser(responseBody)
                userCache = user
                _user.value = ResultState.Success(user)
            }

            override fun onFailure(call: Call<ReqresUserApiResponse>, t: Throwable) {
                _user.value = ResultState.Error(t)
            }
        })
    }

    fun editUser(firstName: String, lastName: String) {
        val user = userCache ?: return

        _isSavingUser.value = true

        networkUserRepository.updateUserById(
            userMapper.toReqresUser(user.copy(firstName = firstName, lastName = lastName)),
            object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    _isSavingUser.value = false
                    if (!response.isSuccessful) {
                        _user.value = ResultState.Error(UserNotFoundException())
                        return
                    }

                    _needCloseScreen.value = true
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    _isSavingUser.value = false
                    _user.value = ResultState.Error(t)
                }

            }
        )
    }

}