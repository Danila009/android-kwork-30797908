package com.example.messenger.ui.screens.usernameChangeScreen

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.messenger.ui.theme.primaryBackground
import com.example.messenger.ui.theme.primaryText
import com.example.messenger.ui.theme.secondaryBackground
import com.example.messenger.ui.theme.tintColor
import com.example.messenger.utils.Result
import com.example.messenger.utils.extensions.launchWhenStarted
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.flow.onEach

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
fun UsernameChangeRoute(
    navController: NavController,
    viewModel: UsernameChangeViewModel = hiltViewModel(),
) {
    var username by remember { mutableStateOf("") }
    var usernameMessage by remember { mutableStateOf("") }

    val systemUiController = rememberSystemUiController()

    viewModel.responseUpdateUsernameResult.onEach { result ->
        when(result){
            is Result.Error -> {
                usernameMessage = ("Ошибка " + result.message)
            }
            is Result.Loading -> {
                usernameMessage = "Загрузка"
            }
            is Result.Success -> navController.navigateUp()
            null -> Unit
        }
    }.launchWhenStarted()

    LaunchedEffect(key1 = Unit, block = {
        systemUiController.setNavigationBarColor(
            color = primaryBackground
        )

        systemUiController.setStatusBarColor(
            color = secondaryBackground
        )
    })

    UsernameChangeScreen(
        username = username,
        usernameMessage = usernameMessage,
        onNewUsername = { newUsername ->
            username = newUsername
        },
        navigateUp = {
            navController.navigateUp()
        },
        updateUsername = {
            viewModel.updateUsername(username = username)
        }
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun UsernameChangeScreen(
    username:String,
    usernameMessage:String,
    onNewUsername:(username:String) -> Unit,
    navigateUp:() -> Unit,
    updateUsername:() -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = secondaryBackground,
                title = {
                    LazyRow {
                        item {
                            Text(
                                text = "Введите ваше имя",
                                color = primaryText
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navigateUp() }) {
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
            LazyColumn {
                item {

                    AnimatedVisibility(visible = usernameMessage.isNotEmpty()) {
                        Text(
                            text = usernameMessage,
                            fontWeight = FontWeight.W900,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp),
                            textAlign = TextAlign.Center
                        )
                    }

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        value = username,
                        onValueChange = onNewUsername,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Go
                        ),
                        label = {
                            Text(
                                text = "Обновить имя пользователя",
                                color = primaryText
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = primaryText,
                            backgroundColor = primaryBackground,
                            focusedIndicatorColor = tintColor,
                            unfocusedLabelColor = tintColor,
                            focusedLabelColor = tintColor,
                            unfocusedIndicatorColor = tintColor,
                            disabledLabelColor = tintColor,
                            cursorColor = tintColor
                        ),
                        keyboardActions = KeyboardActions(
                            onGo = {
                                updateUsername()
                            }
                        )
                    )
                }
            }
        }
    }
}
