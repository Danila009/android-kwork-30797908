package com.example.messenger.ui.screens.profileScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.messenger.R
import com.example.messenger.data.firebase.user.model.User
import com.example.messenger.ui.navigation.Screen
import com.example.messenger.ui.theme.primaryBackground
import com.example.messenger.ui.theme.primaryText
import com.example.messenger.ui.theme.secondaryBackground
import com.example.messenger.ui.theme.tintColor
import com.example.messenger.ui.view.Animation
import com.example.messenger.ui.view.LoadingAnimation
import com.example.messenger.ui.view.LottieAnimation
import com.example.messenger.utils.extensions.launchWhenStarted
import com.example.messenger.utils.Result
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.onEach

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
fun ProfileRoute(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val screenHeightDp = LocalConfiguration.current.screenHeightDp.dp
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp

    val systemUiController = rememberSystemUiController()

    var userResult by remember { mutableStateOf<Result<User>>(Result.Loading()) }

    viewModel.responseUser.onEach { result ->
        userResult = result
    }.launchWhenStarted()

    LaunchedEffect(key1 = Unit, block = {

        systemUiController.setNavigationBarColor(
            color = primaryBackground
        )

        systemUiController.setStatusBarColor(
            color = secondaryBackground
        )

        viewModel.getUserById()
    })

    ProfileScreen(
        userResult = userResult,
        screenWidthDp = screenWidthDp,
        screenHeightDp = screenHeightDp,
        signOut = {
            viewModel.signOut()
            navController.navigate(Screen.SplashScreen.route)
        },
        navigateUp = {
            navController.navigateUp()
        },
        onUsernameChangeScreen = {
            navController.navigate(Screen.UsernameChangeScreen.route)
        },
        onPhoneNumberChangeScreen = {
            navController.navigate(Screen.PhoneNumberChangeScreen.route)
        }
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun ProfileScreen(
    userResult: Result<User>,
    screenWidthDp: Dp,
    screenHeightDp: Dp,
    navigateUp:() -> Unit,
    onUsernameChangeScreen:() -> Unit,
    onPhoneNumberChangeScreen:() -> Unit,
    signOut:() -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = secondaryBackground,
                title = {
                    userResult.data?.let { user ->
                        Text(
                            text = user.username ?: ("+" + user.phone),
                            color = primaryText,
                            fontWeight = FontWeight.W900
                        )
                    }
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
            LazyColumn {
                when(userResult){
                    is Result.Error -> {
                        item {
                            Column(
                                modifier = Modifier.size(
                                    width = screenWidthDp,
                                    height = screenHeightDp
                                ),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Ошибка: ${userResult.message}",
                                    color = Color.Red,
                                    fontWeight = FontWeight.W900
                                )
                            }
                        }
                    }
                    is Result.Loading -> {
                        item {
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
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .size(80.dp)
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                LoadingAnimation(
                                    modifier = Modifier
                                        .padding(5.dp),
                                )
                            }
                        }
                    }
                    is Result.Success -> {
                        item {
                            userResult.data?.let { user ->

                                Divider()

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(secondaryBackground)
                                        .clickable {
//                                            onPhoneNumberChangeScreen()
                                        }
                                ) {
                                    Text(
                                        text = "+" + user.phone,
                                        fontWeight = FontWeight.W900,
                                        modifier = Modifier
                                            .padding(5.dp),
                                        color = primaryText,
                                        fontSize = 14.sp
                                    )

                                    Text(
                                        text = "Номер телефона",
                                        fontWeight = FontWeight.W200,
                                        modifier = Modifier
                                            .padding(5.dp),
                                        color = primaryText,
                                        fontSize = 13.sp
                                    )
                                }

                                Divider()

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(secondaryBackground)
                                        .clickable { onUsernameChangeScreen() }
                                ) {
                                    Text(
                                        text = user.username ?: "Имя пользователя отсутствует",
                                        fontWeight = FontWeight.W900,
                                        modifier = Modifier
                                            .padding(5.dp),
                                        color = primaryText,
                                        fontSize = 14.sp
                                    )

                                    Text(
                                        text = "Вкладка для изменения имени пользователя",
                                        fontWeight = FontWeight.W200,
                                        modifier = Modifier
                                            .padding(5.dp),
                                        color = primaryText,
                                        fontSize = 13.sp
                                    )
                                }

                                Divider()

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
            }
        }
    }
}