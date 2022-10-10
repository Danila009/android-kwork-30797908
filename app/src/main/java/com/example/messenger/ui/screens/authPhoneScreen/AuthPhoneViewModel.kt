package com.example.messenger.ui.screens.authPhoneScreen

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.example.messenger.data.firebase.auth.dataSource.AuthDataSource
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthPhoneViewModel @Inject constructor(
    private val authDataSource: AuthDataSource
):ViewModel() {

    fun sendMessageAuthPhone(
        phone:String,
        activity:Activity,
        onVerificationCompleted:(PhoneAuthCredential) -> Unit = {},
        onVerificationFailed:(FirebaseException) -> Unit = {},
        onCodeSent:(otp: String, resendingToken: PhoneAuthProvider.ForceResendingToken) -> Unit = { _, _ ->}
    ){
        authDataSource.sendMessageAuthPhone(
            phone = phone,
            activity = activity,
            onVerificationCompleted = onVerificationCompleted,
            onVerificationFailed = onVerificationFailed,
            onCodeSent = onCodeSent
        )
    }
}