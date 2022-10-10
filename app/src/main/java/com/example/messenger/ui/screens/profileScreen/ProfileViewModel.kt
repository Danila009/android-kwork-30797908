package com.example.messenger.ui.screens.profileScreen

import androidx.lifecycle.ViewModel
import com.example.messenger.data.firebase.auth.dataSource.AuthDataSource
import com.example.messenger.data.firebase.user.dataStore.UserDataStore
import com.example.messenger.data.firebase.user.model.User
import com.example.messenger.utils.Result
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userDataStore: UserDataStore,
    private val authDataSource: AuthDataSource
):ViewModel() {

    private val _responseUser = MutableStateFlow<Result<User>>(Result.Loading())
    val responseUser = _responseUser.asStateFlow()

    fun insertUser(
        user: User,
        onCompleteListener:(Task<Void>) -> Unit = {}
    ){
        userDataStore.insertUser(
            user = user,
            onCompleteListener = onCompleteListener
        )
    }

    fun signOut(){
        authDataSource.signOut()
    }

    fun getUserById(){
        userDataStore.getUserById(
            onSuccessListener = { user ->
                _responseUser.value = Result.Success(user)
            },
            onFailureListener = { exception ->
                _responseUser.value = Result.Error(exception.message.toString())
            }
        )
    }
}