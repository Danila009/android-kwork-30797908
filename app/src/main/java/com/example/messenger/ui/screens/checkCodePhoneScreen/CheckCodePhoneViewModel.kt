package com.example.messenger.ui.screens.checkCodePhoneScreen

import androidx.lifecycle.ViewModel
import com.example.messenger.data.firebase.auth.dataSource.AuthDataSource
import com.example.messenger.data.firebase.phone.dataStore.PhoneDataStore
import com.example.messenger.data.firebase.user.dataStore.UserDataStore
import com.example.messenger.data.firebase.user.model.User
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Deprecated
import javax.inject.Inject

@HiltViewModel
class CheckCodePhoneViewModel @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val userDataStore: UserDataStore,
    private val phoneDataStore: PhoneDataStore
):ViewModel() {

    fun getUserByPhone(
        phoneNumber:String,
        onSuccessListener:(String?) -> Unit
    ){
        phoneDataStore.getPhone(
            phoneNumber = phoneNumber,
            onSuccessListener = { onSuccessListener(it) }
        )
    }

    fun verificationPhoneCode(
        verificationCode:String,
        code:String,
        onCompleteListener:(isSuccessful:Boolean) -> Unit = {}
    ){
        authDataSource.verificationPhoneCode(
            verificationCode,
            code,
            onCompleteListener
        )
    }

    fun addUser(
        user: User,
        onCompleteListener:(Task<Void>) -> Unit = {}
    ){
        userDataStore.insertUser(
            user = user,
            onCompleteListener = onCompleteListener
        )
    }

    fun addPhone(
        phone:String,
        onSuccessListener:() -> Unit = {},
        onFailureListener:() -> Unit = {}
    ){
        phoneDataStore.addPhone(
            phone = phone,
            onSuccessListener = onSuccessListener,
            onFailureListener = onFailureListener
        )
    }


}