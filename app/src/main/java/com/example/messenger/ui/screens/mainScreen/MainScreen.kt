@file:OptIn(ExperimentalPermissionsApi::class)

package com.example.messenger.ui.screens.mainScreen

import android.Manifest
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.messenger.R
import com.example.messenger.data.firebase.mainList.model.MainListItem
import com.example.messenger.ui.navigation.Screen
import com.example.messenger.ui.theme.primaryBackground
import com.example.messenger.ui.theme.primaryText
import com.example.messenger.ui.theme.secondaryBackground
import com.example.messenger.ui.view.Animation
import com.example.messenger.ui.view.LoadingAnimation
import com.example.messenger.ui.view.LottieAnimation
import com.example.messenger.utils.extensions.asTimeFormat
import com.example.messenger.utils.extensions.launchWhenStarted
import com.example.messenger.utils.Result
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.flow.onEach

private enum class DrawerContentButton(
    val title:String,
    val screen:Screen,
    val icon:ImageVector
) {
    PROFILE("Профиль",Screen.ProfileScreen,Icons.Default.Person),
    Users("Пользователи",Screen.UsersScreen,Icons.Default.Person),
    CONTACTS("Контакты",Screen.ContactsListScreen,Icons.Default.Phone),
    SETTINGS("Настройки",Screen.SettingsScreen,Icons.Default.Settings)
}

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
fun MainRoute(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val systemUiController = rememberSystemUiController()

    var mainListResult by remember { mutableStateOf<Result<List<MainListItem>>>(Result.Loading()) }

    viewModel.responseMainList.onEach { result ->
        mainListResult = result
    }.launchWhenStarted()

    LaunchedEffect(key1 = Unit, block = {
        systemUiController.setNavigationBarColor(
            color = primaryBackground
        )

        systemUiController.setStatusBarColor(
            color = secondaryBackground
        )

        viewModel.getMailList()
    })

    MainScreen(
        navController = navController,
        mainListResult = mainListResult,
        mainListRefresh = {
            viewModel.getMailList()
        }
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun MainScreen(
    navController: NavController,
    mainListResult: Result<List<MainListItem>>,
    mainListRefresh:() -> Unit
) {
    val context = LocalContext.current

    val screenHeightDp = LocalConfiguration.current.screenHeightDp.dp
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp

    val readContactsPermissions =
        rememberPermissionState(permission = Manifest.permission.READ_CONTACTS)

    val swipeRefreshState = rememberSwipeRefreshState(false)
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        drawerBackgroundColor = secondaryBackground,
        drawerContentColor = secondaryBackground,
        topBar = {
            TopAppBar(
                backgroundColor = secondaryBackground,
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        androidx.compose.foundation.Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )

                        Spacer(modifier = Modifier.widthIn(15.dp))

                        Text(
                            text = "SIBALUX",
                            color = primaryText,
                            fontWeight = FontWeight.W900
                        )
                    }
                },
                navigationIcon = {
                    Row {
                        IconButton(onClick = {
                            scope.launch {
                                scaffoldState.drawerState.open()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = null,
                                tint = primaryText
                            )
                        }
                    }
                }
            )
        },
        drawerContent = {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    androidx.compose.foundation.Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(5.dp)
                            .size(80.dp)
                    )

                    DrawerContentButton.values().forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp)
                                .clickable {
                                    scope.launch {
                                        if (
                                            item ==
                                            DrawerContentButton.CONTACTS
                                            && !readContactsPermissions.status.isGranted
                                        ) {
                                            Toast
                                                .makeText(
                                                    context,
                                                    "Нет разрешения для получения контактов, \n" +
                                                            "дайте разрешения в настройка телефона",
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()

                                            readContactsPermissions.launchPermissionRequest()
                                        } else {
                                            scaffoldState.drawerState.close()
                                            navController.navigate(item.screen.route)
                                        }
                                    }
                                }
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null,
                                modifier = Modifier.padding(5.dp),
                                tint = primaryText
                            )


                            Spacer(modifier = Modifier.width(40.dp))

                            Text(
                                text = item.title,
                                modifier = Modifier.padding(5.dp),
                                color = primaryText
                            )
                        }
                    }
                }
            }
        }
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = primaryBackground
        ) {
            SwipeRefresh(
                modifier = Modifier.fillMaxSize(),
                state = swipeRefreshState,
                onRefresh = { mainListRefresh() }
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {

                    if (mainListResult is Result.Error){
                        item {
                            Column(
                                modifier = Modifier.width(
                                    screenWidthDp,
                                ),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Ошибка: ${mainListResult.message ?: ""}",
                                    color = Color.Red,
                                    fontWeight = FontWeight.W900
                                )
                            }
                        }
                    }

                    if (
                        mainListResult is Result.Loading
                    ) {
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

                    items(mainListResult.data ?: emptyList()){ item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                navController.navigate(Screen.ChatScreen.arguments(
                                    receivingUserId = item.id
                                ))
                            }
                        ) {
                            //Image

                            Column {

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = item.username ?: ("+" + item.phone),
                                        modifier = Modifier.padding(5.dp),
                                        fontWeight = FontWeight.W900,
                                        color = primaryText
                                    )

                                    Text(
                                        text = item.dateTime?.asTimeFormat() ?: "",
                                        modifier = Modifier.padding(5.dp),
                                        color = primaryText
                                    )
                                }

                                Text(
                                    text = item.lastMessage,
                                    modifier = Modifier.padding(5.dp),
                                    color = primaryText
                                )
                            }
                        }

                        Divider()
                    }

                    if (
                        mainListResult.data != null &&
                        mainListResult.data.isEmpty()
                    ){
                        item {
                            Column(
                                modifier = Modifier.size(
                                    width = screenWidthDp,
                                    height = screenHeightDp
                                ),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                LottieAnimation(
                                    animation = Animation.Hi,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(max = 400.dp)
                                        .padding(5.dp),
                                )

                                TextButton(onClick = {
                                    navController.navigate(
                                        Screen.UsersScreen.route
                                    )
                                }) {
                                    Text(
                                        text = "Начать!",
                                        color = primaryText,
                                        fontWeight = FontWeight.W900,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}