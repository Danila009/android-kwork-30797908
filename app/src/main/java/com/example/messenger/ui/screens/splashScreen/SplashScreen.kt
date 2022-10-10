package com.example.messenger.ui.screens.splashScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.messenger.R
import com.example.messenger.ui.navigation.Screen
import com.example.messenger.ui.theme.primaryBackground
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@SuppressLint("RememberReturnType")
@Composable
fun SplashScreen(
    navController: NavController
) {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp.dp

    val systemUiController = rememberSystemUiController()

    val firebaseAuth = remember { FirebaseAuth.getInstance() }

    LaunchedEffect(key1 = Unit, block = {
        systemUiController.setSystemBarsColor(
            color = primaryBackground
        )

        delay(2000L)
        navController.navigate(
            if (firebaseAuth.currentUser == null)
                Screen.AuthPhoneScreen.route
            else
                Screen.MainScreen.route
        ){
            popUpTo(Screen.SplashScreen.route){
                inclusive = true
            }
        }
    })

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = primaryBackground
    ) {
        Column(
            modifier = Modifier.size(
                width = screenWidthDp,
                height = screenHeightDp
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            androidx.compose.foundation.Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
        }
    }
}