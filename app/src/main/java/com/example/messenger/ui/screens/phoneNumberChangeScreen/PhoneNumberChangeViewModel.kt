package com.example.messenger.ui.screens.phoneNumberChangeScreen

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.example.messenger.data.firebase.auth.dataSource.AuthDataSource
import com.example.messenger.data.firebase.user.dataStore.UserDataStore
import com.example.messenger.data.firebase.user.model.User
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhoneNumberChangeViewModel @Inject constructor(
    private val userDataStore: UserDataStore,
    private val authDataSource: AuthDataSource
):ViewModel() {

    fun sendMessageAuthPhone(
        phone:String,
        activity: Activity,
        onVerificationFailed:(Exception) -> Unit = {},
        onCodeSent:(otp: String, resendingToken: PhoneAuthProvider.ForceResendingToken) -> Unit = { _, _ ->}
    ){
        try {
            authDataSource.sendMessageAuthPhone(
                phone = phone,
                activity = activity,
                onVerificationFailed = onVerificationFailed,
                onCodeSent = onCodeSent
            )
        }catch (e:Exception){
            onVerificationFailed(e)
        }
    }

    fun getUser(
        onSuccessListener:(user: User) -> Unit = {},
        onFailureListener:(error: Exception) -> Unit = {}
    ){
        try {
            userDataStore.getUserById(
                onSuccessListener = onSuccessListener,
                onFailureListener = onFailureListener
            )
        }catch (e:Exception){
            onFailureListener(e)
        }
    }
}