package com.example.messenger.ui.screens.authPhoneScreen

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.messenger.R
import com.example.messenger.ui.navigation.Screen
import com.example.messenger.ui.theme.primaryBackground
import com.example.messenger.ui.theme.primaryText
import com.example.messenger.ui.theme.secondaryBackground
import com.example.messenger.ui.theme.tintColor
import com.example.messenger.ui.view.Animation
import com.example.messenger.ui.view.LottieAnimation
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun AuthPhoneRoute(
    navController: NavController,
    viewModel: AuthPhoneViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val activity = LocalContext.current as Activity

    val screenHeightDp = LocalConfiguration.current.screenHeightDp.dp
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp

    val systemUiController = rememberSystemUiController()

    var phoneNumber by remember { mutableStateOf("+7") }

    if (phoneNumber.isEmpty())
        phoneNumber = "+"

    LaunchedEffect(key1 = Unit, block = {
        systemUiController.setSystemBarsColor(color = primaryBackground)
    })

    AuthPhoneScreen(
        navController = navController,
        screenHeightDp = screenHeightDp,
        screenWidthDp = screenWidthDp,
        phoneValue = phoneNumber,
        onPhoneValueChange = { phoneNumber = it },
        onClickMessageSend = {
            viewModel.sendMessageAuthPhone(
                phone = phoneNumber,
                activity = activity,
                onCodeSent = { code,_ ->
                    navController.navigate(Screen.CheckCodePhoneScreen.arguments(
                        phone = phoneNumber,
                        code = code
                    ))
                },
                onVerificationCompleted = {

                },
                onVerificationFailed = {
                    phoneNumber = it.message.toString()
                    Toast.makeText(
                        context,
                        "Не получилось отправить код.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    )
}

@Composable
private fun AuthPhoneScreen(
    navController: NavController,
    screenHeightDp:Dp,
    screenWidthDp:Dp,
    phoneValue:String,
    onPhoneValueChange:(String) -> Unit,
    onClickMessageSend:() -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = primaryBackground
    ) {
        LazyColumn {
            item {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 10.dp,
                                top = 10.dp
                            ),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        androidx.compose.foundation.Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(5.dp)
                                .size(120.dp)
                        )
                    }

                    Column(
                        modifier = Modifier
                            .size(
                                width = screenWidthDp,
                                height = screenHeightDp
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        LottieAnimation(
                            animation = Animation.Auth,
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 350.dp)
                                .padding(5.dp),
                        )

                        Text(
                            text = "Ваш номер телефона",
                            color = primaryText,
                            fontWeight = FontWeight.W900,
                            modifier = Modifier.padding(5.dp),
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "Проверьте код страны и ввидите свой номер телефона",
                            color = primaryText,
                            modifier = Modifier.padding(5.dp),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(50.dp))

                        OutlinedTextField(
                            value = phoneValue,
                            onValueChange = onPhoneValueChange,
                            label = { Text(text = "Номер телефона",color = primaryText) },
                            leadingIcon = { Icon(
                                Icons.Filled.Phone,
                                contentDescription = null,
                                tint = primaryText
                            ) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            modifier = Modifier.fillMaxWidth(0.8f),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor = primaryText,
                                backgroundColor = secondaryBackground,
                                cursorColor = primaryText,
                                placeholderColor = primaryText,
                                focusedLabelColor = tintColor,
                                focusedBorderColor = tintColor,
                                unfocusedBorderColor = primaryText
                                ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    onClickMessageSend()
                                }
                            )
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }
    }
}