package com.example.lesson23.screen.networkuserlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lesson23.DataEvent
import com.example.lesson23.ResultState
import com.example.lesson23.db.User
import com.example.lesson23.mappers.ReqresUserToUserMapper
import com.example.lesson23.network.InvalidResponseException
import com.example.lesson23.network.ReqresUserListApiResponse
import com.example.lesson23.repository.NetworkUserRepository
import com.github.javafaker.Faker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkUserListViewModel(
    private val networkUserRepository: NetworkUserRepository,
    private val userMapper: ReqresUserToUserMapper
) :
    ViewModel() {

    private val _users = MutableLiveData<ResultState<List<User>>>()
    val users: LiveData<ResultState<List<User>>> = _users

    private val _openDetailsScreenEvent = MutableLiveData<DataEvent<Long>>()
    val openDetailsScreenEvent: LiveData<DataEvent<Long>> = _openDetailsScreenEvent

    init {
        loadUsers()
    }

    fun loadUsers() {
        _users.value = ResultState.Loading()

        networkUserRepository.getUsers(object : Callback<ReqresUserListApiResponse> {
            override fun onResponse(
                call: Call<ReqresUserListApiResponse>,
                response: Response<ReqresUserListApiResponse>
            ) {
                val responseBody = response.body()

                if (responseBody == null) {
                    _users.value = ResultState.Error(InvalidResponseException())
                    return
                }

                val useFakeData = false

                if (useFakeData) {
                    val users = mutableListOf<User>()
                    for (i in 1..100) {
                        users.add(
                            User(
                                id = i.toLong(),
                                firstName = Faker.instance().name().firstName(),
                                lastName = Faker.instance().name().lastName(),
                                address = Faker.instance().address().fullAddress()
                            )
                        )
                    }

                    _users.value = ResultState.Success(users)
                } else {
                    val userList = userMapper.toUserList(responseBody) +
                            listOf(User(25, "Unknown", "Unknown", "Unknown"))
                    _users.value = ResultState.Success(userList)
                }
            }

            override fun onFailure(call: Call<ReqresUserListApiResponse>, t: Throwable) {
                _users.value = ResultState.Error(t)
            }
        })
    }

    fun onUserClicked(user: User) {
        _openDetailsScreenEvent.value = DataEvent(user.id)
    }

}