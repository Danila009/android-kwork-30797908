package com.example.messenger.ui.screens.authUpdateUserInfoScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.messenger.ui.navigation.Screen
import com.example.messenger.ui.theme.primaryBackground
import com.example.messenger.ui.theme.primaryText
import com.example.messenger.ui.theme.secondaryBackground
import com.example.messenger.ui.theme.tintColor
import com.example.messenger.ui.view.Animation
import com.example.messenger.ui.view.LottieAnimation
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun AuthUpdateUserInfoRoute(
    navController: NavController,
    viewModel: AuthUpdateUserInfoViewModel = hiltViewModel(),
) {
    val screenHeightDp = LocalConfiguration.current.screenHeightDp.dp
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp

    val systemUiController = rememberSystemUiController()

    var username by remember { mutableStateOf("") }

    LaunchedEffect(key1 = Unit, block = {
        systemUiController.setSystemBarsColor(color = primaryBackground)
    })

    AuthUpdateUserInfoScreen(
        screenHeightDp = screenHeightDp,
        screenWidthDp = screenWidthDp,
        username = username,
        onValueChangeUsername = { newUsername ->
            username = newUsername
        },
        onUpdateUsername = {
            viewModel.updateUsername(
                username = username,
                onSuccessListener = {
                    navController.navigate(Screen.MainScreen.route){
                        popUpTo(Screen.AuthUpdateUserInfoScreen.route){
                            inclusive = true
                        }
                    }
                }
            )
        },
        nextScreen = {
            navController.navigate(Screen.MainScreen.route)
        }
    )
}

@Composable
private fun AuthUpdateUserInfoScreen(
    screenHeightDp:Dp,
    screenWidthDp:Dp,
    username:String,
    onValueChangeUsername:(String) -> Unit,
    onUpdateUsername:() -> Unit,
    nextScreen:() -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = primaryBackground
    ) {
        LazyColumn {
            item {
                Column(
                    modifier = Modifier.size(
                        height = screenHeightDp,
                        width = screenWidthDp
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LottieAnimation(
                        animation = Animation.Writing,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 350.dp)
                            .padding(5.dp),
                    )

                    OutlinedTextField(
                        value = username,
                        onValueChange = onValueChangeUsername,
                        modifier = Modifier.padding(5.dp),
                        label = {
                            Text(
                                text = "Имя пользователя",
                                color = primaryText
                            )
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = primaryText,
                            backgroundColor = secondaryBackground,
                            cursorColor = primaryText,
                            placeholderColor = primaryText,
                            focusedLabelColor = tintColor,
                            focusedBorderColor = tintColor
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                onUpdateUsername()
                            }
                        )
                    )

                    Button(
                        modifier = Modifier.padding(5.dp),
                        shape = AbsoluteRoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = tintColor
                        ),
                        onClick = {
                            nextScreen()
                        }
                    ) {
                        Text(text = "Пропустить")
                    }
                }
            }
        }
    }
}