package com.example.messenger.ui.screens.usersScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.messenger.ui.view.Animation
import com.example.messenger.ui.view.Image
import com.example.messenger.ui.view.LoadingAnimation
import com.example.messenger.ui.view.LottieAnimation
import com.example.messenger.utils.Result
import com.example.messenger.utils.extensions.launchWhenStarted
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.flow.onEach

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
fun UsersRoute(
    navController: NavController,
    viewModel: UsersViewModel = hiltViewModel()
) {
    val screenHeightDp = LocalConfiguration.current.screenHeightDp.dp
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp

    val systemUiController = rememberSystemUiController()

    var usersResult by remember { mutableStateOf<Result<List<User>>>(Result.Loading()) }

    viewModel.responseUsers.onEach { result ->
        usersResult = result
    }.launchWhenStarted()

    LaunchedEffect(key1 = Unit, block = {

        systemUiController.setNavigationBarColor(
            color = primaryBackground
        )

        systemUiController.setStatusBarColor(
            color = secondaryBackground
        )

        viewModel.getUsers()
    })

    UsersScreen(
        usersResult = usersResult,
        screenWidthDp = screenWidthDp,
        screenHeightDp = screenHeightDp,
        onChatScreen = { userId ->
            navController.navigate(Screen.ChatScreen.arguments(userId))
        },
        navigateUp = {
            navController.navigateUp()
        }
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun UsersScreen(
    usersResult:Result<List<User>>,
    screenWidthDp:Dp,
    screenHeightDp:Dp,
    onChatScreen:(userId:String) -> Unit,
    navigateUp:() -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = secondaryBackground,
                title = {
                    Text(
                        text = "Пользователи",
                        color = primaryText
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
            LazyColumn {
                when(usersResult){
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
                                    text = "Ошибка: ${usersResult.message}",
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
                        val users = usersResult.data ?: return@LazyColumn

                        items(users){ user ->

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onChatScreen(user.id) }
                            ) {

                                user.photo?.let {
                                    Image(
                                        url = user.photo,
                                        modifier = Modifier
                                            .padding(5.dp)
                                            .size(120.dp)
                                            .clip(AbsoluteRoundedCornerShape(90.dp))
                                    )
                                }

                                Column {
                                    Text(
                                        text = user.username ?: "Имя отсутствует",
                                        color = primaryText,
                                        modifier = Modifier.padding(5.dp),
                                        fontWeight = FontWeight.W900,
                                        fontSize = 14.sp
                                    )

                                    Text(
                                        text = "+" + user.phone,
                                        color = primaryText,
                                        modifier = Modifier.padding(5.dp),
                                        fontWeight = FontWeight.W200,
                                        fontSize = 13.sp
                                    )
                                }
                            }

                            Divider()
                        }
                    }
                }
            }
        }
    }
}