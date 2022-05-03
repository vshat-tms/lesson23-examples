package com.example.lesson23.screen.userdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.lesson23.ResultState
import com.example.lesson23.db.User
import com.example.lesson23.repository.UserRepository

class UserDetailsViewModel(
    private val userId: Long,
    private val userRepository: UserRepository
) : ViewModel() {

    private var currentUserCache: User? = null

    val currentUser: LiveData<ResultState<User>> = userRepository.getUserById(userId)
        .map {
            if (it is ResultState.Success) {
                currentUserCache = it.data
            }
            it
        }

    private val _needCloseScreen = MutableLiveData<Boolean>()
    val needCloseScreen: LiveData<Boolean> = _needCloseScreen

    private val _isSavingUser = MutableLiveData<Boolean>()
    val isSavingUser: LiveData<Boolean> = _isSavingUser

    fun editUser(firstName: String, lastName: String) {
        val user = currentUserCache ?: return

        _isSavingUser.value = true

        userRepository.updateUser(
            user = user.copy(firstName = firstName, lastName = lastName),
            onDoneCallback = {
                _needCloseScreen.postValue(true)
                _isSavingUser.postValue(false)
            }
        )
    }

}