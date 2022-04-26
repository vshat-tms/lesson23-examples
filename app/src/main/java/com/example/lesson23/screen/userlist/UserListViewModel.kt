package com.example.lesson23.screen.userlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lesson23.DataEvent
import com.example.lesson23.Event
import com.example.lesson23.User
import com.example.lesson23.UserRepository

class UserListViewModel : ViewModel() {
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private val _showListEmptyMessageErrorEvent = MutableLiveData<DataEvent<String>>()
    val showListEmptyMessageErrorEvent: LiveData<DataEvent<String>> = _showListEmptyMessageErrorEvent

    private val _openDetailsScreenEvent = MutableLiveData<DataEvent<Long>>()
    val openDetailsScreenEvent: LiveData<DataEvent<Long>> = _openDetailsScreenEvent

    private var userRepositoryListener = object : UserRepository.RepositoryChangeListener {
        override fun onUserListUpdated(newList: List<User>) {
            _users.postValue(newList)
        }
    }

    init {
        UserRepository.addListener(userRepositoryListener)
    }

    override fun onCleared() {
        super.onCleared()
        UserRepository.removeListener(userRepositoryListener)
    }

    fun onRemoveAllClicked() {
//        if(UserRepository.users.isEmpty()) {
//            _showListEmptyMessageErrorEvent.value = DataEvent("error message")
//            return
//        }

        UserRepository.removeAll()
    }

    fun onRemoveLastClicked() {
        UserRepository.removeLast()
    }

    fun onEditSecondClicked() {
//        if (UserRepository.users.size < 2) return
//
//        UserRepository.updateUser(1, User("SECOND", "SECOND", "test test tesst"))
//        _users.value = UserRepository.users
    }

    fun onAddRandomClicked() {
        UserRepository.addRandomUser()
    }

    fun onUserClicked(user: User) {
        _openDetailsScreenEvent.value = DataEvent(user.id)
    }
}