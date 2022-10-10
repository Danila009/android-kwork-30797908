package com.example.messenger.ui.screens.settingsScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.messenger.ui.navigation.Screen
import com.example.messenger.ui.theme.primaryBackground
import com.example.messenger.ui.theme.primaryText
import com.example.messenger.ui.theme.secondaryBackground
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun SettingsRoute(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val systemUiController = rememberSystemUiController()

    LaunchedEffect(key1 = Unit, block = {

        systemUiController.setNavigationBarColor(
            color = primaryBackground
        )

        systemUiController.setStatusBarColor(
            color = secondaryBackground
        )
    })

    SettingsScreen(
        navigateUp = {
            navController.navigateUp()
        },
        signOut = {
            viewModel.signOut()
            navController.navigate(Screen.SplashScreen.route)
        }
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun SettingsScreen(
    navigateUp:() -> Unit,
    signOut:() -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = secondaryBackground,
                title = {
                    Text(
                        text = "Настройки",
                        color = primaryText,
                        fontWeight = FontWeight.W900
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navigateUp()
                    }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = null,
                            tint = primaryText
                        )
                    }
                }
            )
        }
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = primaryBackground
        ) {
            Column {
                Spacer(modifier = Modifier.height(40.dp))

                Divider(
                    color = Color.Red
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(secondaryBackground)
                        .clickable { signOut() }
                ) {
                    Text(
                        text = "Выйти",
                        fontWeight = FontWeight.W900,
                        modifier = Modifier
                            .padding(5.dp),
                        color = primaryText,
                        fontSize = 14.sp
                    )

                    Text(
                        text = "При нажатии вы выйдите из системы," +
                                " и будите переброшены на экран авторизации",
                        fontWeight = FontWeight.W200,
                        modifier = Modifier
                            .padding(5.dp),
                        color = primaryText,
                        fontSize = 13.sp
                    )
                }

                Divider(
                    color = Color.Red
                )
            }
        }
    }
}