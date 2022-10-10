package com.example.messenger.ui.screens.usersScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.messenger.data.firebase.user.dataStore.UserDataStore
import com.example.messenger.data.firebase.user.model.User
import com.example.messenger.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val userDataStore: UserDataStore
):ViewModel() {

    private val _responseUsers = MutableStateFlow<Result<List<User>>>(Result.Loading())
    val responseUsers = _responseUsers.asStateFlow()

    fun getUsers(
    ){
        viewModelScope.launch {
            userDataStore.getUsers(
                onSuccessListener = { users ->
                    _responseUsers.value = Result.Success(data = users)
                },
                onFailureListener = { exception ->
                    _responseUsers.value = Result.Error(exception.message.toString())
                }
            )
        }
    }
}