@file:OptIn(ExperimentalFoundationApi::class, ExperimentalPermissionsApi::class)

package com.example.messenger.ui.screens.contactsListScreen

import android.Manifest
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.messenger.data.database.contacts.model.Contact
import com.example.messenger.ui.navigation.Screen
import com.example.messenger.ui.theme.primaryBackground
import com.example.messenger.ui.theme.primaryText
import com.example.messenger.ui.theme.secondaryBackground
import com.example.messenger.utils.APK
import com.example.messenger.utils.sendSms
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
fun ContactsListRoute(
    navController: NavController,
    viewModel: ContactsListViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()

    var contacts by remember { mutableStateOf(emptyList<Contact>()) }

    var sendSmsAlertDialog by remember { mutableStateOf(false) }
    var contactPhone by remember { mutableStateOf("") }

    val permissions = rememberPermissionState(permission = Manifest.permission.READ_CONTACTS)

    LaunchedEffect(key1 = Unit, block = {

        systemUiController.setNavigationBarColor(
            color = primaryBackground
        )

        systemUiController.setStatusBarColor(
            color = secondaryBackground
        )

        permissions.launchPermissionRequest()
    })

    if (permissions.status.isGranted){
        contacts = viewModel.getContacts
    }else {
        navController.navigateUp()
    }

    ContactsListScreen(
        contacts = contacts,
        sendSmsAlertDialog = sendSmsAlertDialog,
        contactPhone = contactPhone,
        getPhone = { phoneNumber ->
            contactPhone = phoneNumber
            viewModel.getPhone(
                phoneNumber,
                { userId ->
                    if (userId == null){
                        sendSmsAlertDialog = true
                    }else {
                        navController.navigate(Screen.ChatScreen.arguments(
                            receivingUserId = userId
                        ))
                    }
                },
                { sendSmsAlertDialog = true }
            )
        },
        clearContactPhone = {
            contactPhone = ""
            sendSmsAlertDialog = false
        },
        navigateUp = { navController.navigateUp() }
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "NewApi")
@Composable
private fun ContactsListScreen(
    contacts:List<Contact>,
    sendSmsAlertDialog:Boolean,
    contactPhone:String,
    getPhone:(phoneNumber:String) -> Unit,
    clearContactPhone:() -> Unit,
    navigateUp:() -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = secondaryBackground,
                title = {
                    Text(
                        text = "Контакты",
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
            if (contactPhone.isNotEmpty() && sendSmsAlertDialog){
                SendSmsAlertDialog(
                    phoneNumber = contactPhone,
                    onDismissRequest = clearContactPhone
                )
            }

            LazyColumn {

                val grouped = contacts.groupBy { it.name[0] }

                grouped.forEach { (initial, items) ->
                    stickyHeader {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = initial.toString(),
                                modifier = Modifier.padding(10.dp),
                                color = primaryText,
                                fontWeight = FontWeight.W900,
                                fontSize = 20.sp
                            )
                        }
                    }

                    items(items){ contact ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp)
                                .background(secondaryBackground)
                                .clickable {
                                    getPhone(contact.phone)
                                }
                        ) {
                            Text(
                                text = contact.name,
                                modifier = Modifier.padding(5.dp),
                                fontWeight = FontWeight.W900,
                                color = primaryText
                            )

                            Text(
                                text = contact.phone,
                                modifier = Modifier.padding(5.dp),
                                fontWeight = FontWeight.W100,
                                color = primaryText
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SendSmsAlertDialog(
    phoneNumber:String,
    onDismissRequest:() -> Unit
) {
    val context = LocalContext.current

    var smsSend by remember { mutableStateOf(false) }

    if (smsSend){
        val send = sendSms(
            message = "Скачай приложения для общения \n $APK",
            phoneNumber = phoneNumber
        )

        if (send){
            Toast.makeText(
                context,
                "Сообщение отправлено",
                Toast.LENGTH_LONG
            ).show()

            smsSend = false
            onDismissRequest()
        } else {
            Toast.makeText(
                context,
                "Не удалось отправить сообщения",
                Toast.LENGTH_LONG
            ).show()

            smsSend = false
        }
    }

    AlertDialog(
        backgroundColor = primaryBackground,
        shape = AbsoluteRoundedCornerShape(15.dp),
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = "У этого пользователя нет приложения.\n Вы можете отправит ссылку для скачивания приложения.",
                color = primaryText,
                fontWeight = FontWeight.W900,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(70.dp))
        },
        buttons = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(
                    modifier = Modifier.padding(10.dp),
                    shape = AbsoluteRoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = secondaryBackground
                    ),
                    onClick = { onDismissRequest() }
                ) {
                    Text(
                        text = "Отмена",
                        color = primaryText,
                        textAlign = TextAlign.Center
                    )
                }

                Button(
                    modifier = Modifier.padding(10.dp),
                    shape = AbsoluteRoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = secondaryBackground
                    ),
                    onClick = {
                        smsSend = true
                    }
                ) {
                    Text(
                        text = "Отправить ссылку",
                        color = primaryText,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    )
}