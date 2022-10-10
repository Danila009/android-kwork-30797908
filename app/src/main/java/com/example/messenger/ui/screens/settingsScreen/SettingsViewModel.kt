package com.example.messenger.ui.screens.settingsScreen

import androidx.lifecycle.ViewModel
import com.example.messenger.data.firebase.auth.dataSource.AuthDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authDataSource: AuthDataSource
):ViewModel() {

    fun signOut(){
        authDataSource.signOut()
    }
}