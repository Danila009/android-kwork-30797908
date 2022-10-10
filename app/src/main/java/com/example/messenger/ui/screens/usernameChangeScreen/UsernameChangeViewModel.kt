package com.example.messenger.ui.screens.usernameChangeScreen

import androidx.lifecycle.ViewModel
import com.example.messenger.data.firebase.user.dataStore.UserDataStore
import com.example.messenger.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class UsernameChangeViewModel @Inject constructor(
    private val userDataStore: UserDataStore
):ViewModel() {
    private val _responseUpdateUsernameResult = MutableStateFlow<Result<Unit?>?>(null)
    val responseUpdateUsernameResult = _responseUpdateUsernameResult.asStateFlow()


    fun updateUsername(
        username:String
    ){
        try {
            userDataStore.updateUsername(
                username = username,
                onSuccessListener = {
                    _responseUpdateUsernameResult.value = Result.Success(Unit)
                },
                onFailureListener = { e ->
                    _responseUpdateUsernameResult.value = Result.Error(e.message.toString())
                }
            )
        }catch (e:Exception){
            _responseUpdateUsernameResult.value = Result.Error(e.message.toString())
        }
    }
}