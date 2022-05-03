package com.example.lesson23.screen.userlist

import androidx.lifecycle.*
import com.example.lesson23.DataEvent
import com.example.lesson23.db.User
import com.example.lesson23.repository.UserQuery
import com.example.lesson23.repository.UserRepository
import com.example.lesson23.repository.UserSortOrder


class UserListViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _showListEmptyMessageErrorEvent = MutableLiveData<DataEvent<String>>()
    val showListEmptyMessageErrorEvent: LiveData<DataEvent<String>> =
        _showListEmptyMessageErrorEvent

    private val _openDetailsScreenEvent = MutableLiveData<DataEvent<Long>>()
    val openDetailsScreenEvent: LiveData<DataEvent<Long>> = _openDetailsScreenEvent

    private var userQuery = UserQuery()
    private val userQueryLiveData = MutableLiveData(userQuery)

    val users: LiveData<List<User>> = userQueryLiveData.switchMap {
        userRepository.getAllUsers(it)
    }

    fun setSortOrder(sortOrder: UserSortOrder) {
        userQuery = userQuery.copy(sortOrder = sortOrder)
        userQueryLiveData.value = userQuery
    }

    fun onSearchQueryChanged(query: String) {
        userQuery = userQuery.copy(searchQuery = query)
        userQueryLiveData.value = userQuery
    }


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