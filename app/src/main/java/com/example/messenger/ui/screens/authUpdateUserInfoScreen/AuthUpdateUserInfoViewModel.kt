package com.example.messenger.ui.screens.authUpdateUserInfoScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.messenger.data.firebase.user.dataStore.UserDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthUpdateUserInfoViewModel @Inject constructor(
    private val userDataStore: UserDataStore
):ViewModel() {

    fun updateUsername(
        username:String,
        onSuccessListener:() -> Unit = {},
        onFailureListener:(Exception) -> Unit = {}
    ){
        viewModelScope.launch {
            userDataStore.updateUsername(
                username = username,
                onSuccessListener = onSuccessListener,
                onFailureListener = onFailureListener
            )
        }
    }
}