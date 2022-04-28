package com.example.lesson23.screen.userlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lesson23.DataEvent
import com.example.lesson23.db.User
import com.example.lesson23.repository.UserRepository

class UserListViewModel(private val userRepository: UserRepository) : ViewModel() {
    val users: LiveData<List<User>> = userRepository.getAllUsers()

    private val _showListEmptyMessageErrorEvent = MutableLiveData<DataEvent<String>>()
    val showListEmptyMessageErrorEvent: LiveData<DataEvent<String>> =
        _showListEmptyMessageErrorEvent

    private val _openDetailsScreenEvent = MutableLiveData<DataEvent<Long>>()
    val openDetailsScreenEvent: LiveData<DataEvent<Long>> = _openDetailsScreenEvent

    fun onRemoveAllClicked() {
//        if(UserRepository.users.isEmpty()) {
//            _showListEmptyMessageErrorEvent.value = DataEvent("error message")
//            return
//        }


        userRepository.deleteAll()
    }

    fun onRemoveLastClicked() {
//        OldUserRepository.removeLast()
    }

    fun onEditSecondClicked() {
//        if (UserRepository.users.size < 2) return
//
//        UserRepository.updateUser(1, User("SECOND", "SECOND", "test test tesst"))
//        _users.value = UserRepository.users
    }

    fun onAddRandomClicked() {
        userRepository.addRandomUser()
    }

    fun onUserClicked(user: User) {
        _openDetailsScreenEvent.value = DataEvent(user.id)
    }
}