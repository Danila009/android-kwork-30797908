package com.example.messenger.ui.screens.phoneNumberChangeScreen

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.messenger.ui.theme.primaryBackground
import com.example.messenger.ui.theme.primaryText
import com.example.messenger.ui.theme.secondaryBackground
import com.example.messenger.ui.theme.tintColor
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun PhoneNumberChangeRoute(
    navController: NavController,
    viewModel: PhoneNumberChangeViewModel = hiltViewModel()
) {
    val activity = LocalContext.current as Activity

    val systemUiController = rememberSystemUiController()

    var phoneNumberMessage by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    LaunchedEffect(key1 = Unit, block = {
        systemUiController.setNavigationBarColor(
            color = primaryBackground
        )

        systemUiController.setStatusBarColor(
            color = secondaryBackground
        )

        viewModel.getUser(
            onSuccessListener = { user ->
                phoneNumber = user.phone
            }
        )
    })

    PhoneNumberChangeScreen(
        phoneNumber = phoneNumber,
        phoneNumberMessage = phoneNumberMessage,
        navigateUp = {
            navController.navigateUp()
        },
        onPhoneNumber = { newPhoneNumber ->
            phoneNumber = newPhoneNumber
        },
        updatePhoneNumber = {
            viewModel.sendMessageAuthPhone(
                phone = phoneNumber,
                activity = activity,
                onCodeSent = { code, _ ->

                },
                onVerificationFailed = { e ->
                    phoneNumberMessage = e.message.toString()
                }
            )
        }
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun PhoneNumberChangeScreen(
    phoneNumber:String,
    phoneNumberMessage:String,
    navigateUp:() -> Unit,
    onPhoneNumber:(phoneNumber:String) -> Unit,
    updatePhoneNumber:() -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = secondaryBackground,
                title = {
                    LazyRow {
                        item {
                            Text(
                                text = "Обновить номер телефона",
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
                    AnimatedVisibility(visible = phoneNumberMessage.isNotEmpty()) {
                        Text(
                            text = phoneNumberMessage,
                            fontWeight = FontWeight.W900,
                            modifier = Modifier.padding(5.dp)
                        )
                    }

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        value = phoneNumber,
                        onValueChange = onPhoneNumber,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Go
                        ),
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = primaryText,
                            backgroundColor = primaryBackground,
                            focusedIndicatorColor = tintColor,
                            unfocusedLabelColor = tintColor,
                            focusedLabelColor = tintColor,
                            unfocusedIndicatorColor = tintColor
                        ),
                        keyboardActions = KeyboardActions(
                            onGo = {
                                updatePhoneNumber()
                            }
                        )
                    )
                }
            }
        }
    }
}