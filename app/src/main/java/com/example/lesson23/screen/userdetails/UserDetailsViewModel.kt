package com.example.lesson23.screen.userdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lesson23.User
import com.example.lesson23.UserRepository

class UserDetailsViewModel(private val userId: Long) : ViewModel() {
    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> = _currentUser

    private val _isUserMissingError = MutableLiveData<Boolean>()
    val isUserMissingError: LiveData<Boolean> = _isUserMissingError

    private val _needCloseScreen = MutableLiveData<Boolean>()
    val needCloseScreen: LiveData<Boolean> = _needCloseScreen

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSavingUser = MutableLiveData<Boolean>()
    val isSavingUser: LiveData<Boolean> = _isSavingUser


    init {
        _isLoading.value = true

        UserRepository.getUserById(userId) {
            _isUserMissingError.postValue(it == null)
            _currentUser.postValue(it)
            _isLoading.postValue(false)
        }
    }

    fun editUser(firstName: String, lastName: String) {
        _isSavingUser.value = true

        val address = currentUser.value?.address ?: ""
        UserRepository.updateUserById(userId, User(firstName, lastName, address, userId)) {
            _needCloseScreen.postValue(true)
            _isSavingUser.postValue(false)
        }
    }

}