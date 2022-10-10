@file:OptIn(ExperimentalPermissionsApi::class, ExperimentalAnimationApi::class)

package com.example.messenger

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.messenger.ui.navigation.BaseNavHost
import com.example.messenger.ui.navigation.Screen
import com.example.messenger.ui.theme.MessengerTheme
import com.example.messenger.ui.theme.primaryBackground
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navHostController = rememberAnimatedNavController()

            val permissions = rememberMultiplePermissionsState(permissions = listOf(
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.SEND_SMS,
            ))

            LaunchedEffect(key1 = Unit, block = {
                permissions.launchMultiplePermissionRequest()
            })

            MessengerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = primaryBackground
                ) {
                    BaseNavHost(
                        navHostController = navHostController,
                        startDestination = Screen.SplashScreen
                    )
                }
            }
        }
    }
}
