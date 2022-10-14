package com.example.messenger.ui.screens.chatScreen

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.messenger.R
import com.example.messenger.data.firebase.mainList.model.MainListItem
import com.example.messenger.data.firebase.message.model.Message
import com.example.messenger.data.firebase.message.model.MessageType
import com.example.messenger.data.firebase.user.model.User
import com.example.messenger.ui.navigation.Screen
import com.example.messenger.ui.screens.chatScreen.view.AttachFileCard
import com.example.messenger.ui.theme.*
import com.example.messenger.ui.view.LoadingAnimation
import com.example.messenger.utils.extensions.launchWhenStarted
import com.example.messenger.utils.Result
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.onEach

//debug
var imageUrl = ""

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
fun ChatRout(
    navController: NavController,
    viewModel: ChatViewModel = hiltViewModel(),
    receivingUserId: String,
) {
    val screenHeightDp = LocalConfiguration.current.screenHeightDp.dp
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp

    val systemUiController = rememberSystemUiController()
    val lazyListState = rememberLazyListState()

    val auth = remember { FirebaseAuth.getInstance() }

    var messageText by remember { mutableStateOf("") }

    var attachFileCardVisible by remember { mutableStateOf(false) }

    var receivingUserResult by remember { mutableStateOf<Result<User>>(Result.Loading()) }
    var messagesResult by remember { mutableStateOf<Result<List<Message>>>(Result.Loading()) }

    val images = remember<SnapshotStateList<Uri>>(::mutableStateListOf)
    val bitmapImages = remember<SnapshotStateList<Bitmap>>(::mutableStateListOf)

    var loadingMessage by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0L) }
    var totalKb by remember { mutableStateOf(0L) }
    var transferredKb by remember { mutableStateOf(0L) }

    viewModel.responseReceivingUser.onEach { result ->
        receivingUserResult = result
    }.launchWhenStarted()

    viewModel.responseMessages.onEach {result ->
        messagesResult = result
    }.launchWhenStarted()

    LaunchedEffect(key1 = Unit, block = {

        systemUiController.setNavigationBarColor(
            color = secondaryBackground
        )

        systemUiController.setStatusBarColor(
            color = secondaryBackground
        )

        viewModel.getUserById(receivingUserId = receivingUserId)
        viewModel.getMessages(receivingUserId = receivingUserId)
    })

    LaunchedEffect(key1 = messagesResult.data?.size, block = {
        messagesResult.data?.size?.let { size ->
            if (size > 0){
                lazyListState.animateScrollToItem(size - 1)
            }
        }
    })

    ChatScreen(
        navController = navController,
        lazyListState = lazyListState,
        images = images,
        bitmapImages = bitmapImages,
        loadingMessage = loadingMessage,
        progress = progress,
        totalKb = totalKb,
        transferredKb = transferredKb,
        attachFileCardVisible = attachFileCardVisible,
        userId = auth.currentUser?.uid ?: return,
        screenWidthDp = screenWidthDp,
        screenHeightDp = screenHeightDp,
        messageValue = messageText,
        receivingUserResult = receivingUserResult,
        messagesResult = messagesResult,
        onMessageValueChange = { messageText = it },
        sendMessage = { text,type,sendImages ->
            viewModel.sendMessage(message = Message(
                text = text,
                type = type.name,
            ),receivingUserId = receivingUserId,
                images = sendImages,
                bitmapImages = bitmapImages,
                onProgress = { newProgress, newTotalKb, newTransferredKb ->

                    loadingMessage = true

                    progress = newProgress
                    totalKb = newTotalKb
                    transferredKb = newTransferredKb

                    if(newTotalKb == newTransferredKb){
                        loadingMessage = false
                    }
                }
            )

            viewModel.sendToMainList(
                mainListItem = MainListItem(
                    id = receivingUserId,
                    phone = receivingUserResult.data?.phone ?: return@ChatScreen,
                    username = receivingUserResult.data?.username,
                    lastMessage = if (text.isEmpty() &&
                        (sendImages.isNotEmpty() || bitmapImages.isNotEmpty())) "Изображения"
                            else
                                text
                )
            )

            images.removeAll { true }
            bitmapImages.removeAll { true }
            attachFileCardVisible = false
            messageText = ""
        },
        onAttachFileClick = { attachFileCardVisible = !attachFileCardVisible },
        onImage = {
            attachFileCardVisible = false
            bitmapImages.add(it)
        },
        onImages = {
            attachFileCardVisible = false
            images.addAll(it)
        },
        onClearImage = { uri,clearBitmapImages ->
            images.remove(uri)
            bitmapImages.remove(clearBitmapImages)
        },
        onClearImages = {
            images.removeAll { true }
            bitmapImages.removeAll { true }
        }
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun ChatScreen(
    navController: NavController,
    lazyListState:LazyListState,
    attachFileCardVisible:Boolean,
    loadingMessage:Boolean,
    progress:Long,
    totalKb:Long,
    transferredKb:Long,
    images:List<Uri>,
    bitmapImages:List<Bitmap>,
    userId:String,
    screenWidthDp: Dp,
    screenHeightDp: Dp,
    messageValue:String,
    receivingUserResult:Result<User>,
    messagesResult: Result<List<Message>>,
    onMessageValueChange:(String) -> Unit,
    sendMessage:(text:String,type:MessageType,images:List<Uri>) -> Unit,
    onAttachFileClick:() -> Unit,
    onImage:(Bitmap) -> Unit,
    onImages:(List<Uri>) -> Unit,
    onClearImage:(images:Uri?,bitmapImages:Bitmap?) -> Unit,
    onClearImages:() -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = secondaryBackground,
                title = {
                    receivingUserResult.data?.let { user ->
                        Column {
                            LazyRow {
                                item {
                                    Text(
                                        text = user.username ?: ("+" + user.phone),
                                        modifier = Modifier.padding(5.dp),
                                        fontWeight = FontWeight.W800,
                                        color = primaryText,
                                        fontSize = 15.sp
                                    )
                                }
                            }

                            Text(
                                text = if(user.onlineStatus) "В сети"
                                else "Не в сети",
                                fontWeight = FontWeight.W200,
                                color = primaryText,
                                fontSize = 13.sp
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = null,
                            tint = primaryText
                        )
                    }
                }
            )
        }, bottomBar = {
            Box(modifier = Modifier
                .fillMaxWidth()
                .background(secondaryBackground)
                .heightIn(max = screenHeightDp / 4)
            ){
                Column {

                    AnimatedVisibility(visible = attachFileCardVisible) {
                        AttachFileCard(
                            onImage = onImage,
                            onImages = onImages
                        )
                    }

                    AnimatedVisibility(
                        visible = (images.isNotEmpty() || bitmapImages.isNotEmpty()) || loadingMessage
                    ) {
                        LazyRow(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            item {
                                if(loadingMessage){
                                    Column(
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = "Отправка",
                                            color = primaryText,
                                            modifier = Modifier.padding(5.dp)
                                        )

                                        Text(
                                            text = "$progress/100 %",
                                            color = primaryText,
                                            modifier = Modifier.padding(5.dp)
                                        )

                                        Text(
                                            text = "$transferredKb/$totalKb Kb",
                                            color = primaryText,
                                            modifier = Modifier.padding(5.dp)
                                        )
                                    }
                                }
                            }

                            items(images){ image ->
                                Box {
                                    com.example.messenger.ui.view.Image(
                                        url = image,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .padding(5.dp)
                                            .size(60.dp)
                                    )

                                    Row(
                                        modifier = Modifier.padding(start = 50.dp),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        IconButton(
                                            onClick = { onClearImage(image,null) }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Clear,
                                                contentDescription = null,
                                                tint = Color.Red
                                            )
                                        }
                                    }
                                }
                            }

                            items(bitmapImages){ image ->
                                Box {
                                    com.example.messenger.ui.view.Image(
                                        url = image,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .padding(5.dp)
                                            .size(60.dp)
                                    )

                                    Row(
                                        modifier = Modifier.padding(start = 50.dp),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        IconButton(
                                            onClick = { onClearImage(null,image) }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Clear,
                                                contentDescription = null,
                                                tint = Color.Red
                                            )
                                        }
                                    }
                                }
                            }

                            item {
                                if(!loadingMessage){
                                    TextButton(onClick = { onClearImages() }) {
                                        Text(
                                            text = "Закрыть\n все",
                                            modifier = Modifier.padding(5.dp),
                                            color = Color.Red,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextField(
                            value = messageValue,
                            onValueChange = onMessageValueChange,
                            modifier = Modifier.fillMaxWidth(0.8f),
                            placeholder = {
                                Text(
                                    text = "Сообщение",
                                    color = primaryText
                                )
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = secondaryBackground,
                                focusedLabelColor = secondaryBackground,
                                textColor = primaryText,
                                cursorColor = tintColor,
                                unfocusedLabelColor = secondaryBackground,
                                focusedIndicatorColor = secondaryBackground,
                                unfocusedIndicatorColor = secondaryBackground
                            )
                        )

                        AnimatedVisibility(
                            visible =
                            messageValue.isNotEmpty() || images.isNotEmpty() || bitmapImages.isNotEmpty()
                        ) {
                            IconButton(onClick = {
                                sendMessage(messageValue, MessageType.TEXT,images)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Send,
                                    contentDescription = null,
                                    modifier = Modifier.padding(5.dp),
                                    tint = primaryText
                                )
                            }
                        }

                        AnimatedVisibility(visible = messageValue.isEmpty()) {
                            Row {
                                IconButton(onClick = { onAttachFileClick() }) {
                                    Icon(
                                        imageVector = Icons.Default.AttachFile,
                                        contentDescription = null,
                                        modifier = Modifier.padding(5.dp),
                                        tint = primaryText
                                    )
                                }
                            }
                        }

                        AnimatedVisibility(
                            visible = messageValue.isEmpty() && images.isEmpty()
                        ) {
                            IconButton(onClick = { onAttachFileClick() }) {
                                Icon(
                                    imageVector = Icons.Default.Mic,
                                    contentDescription = null,
                                    modifier = Modifier.padding(5.dp),
                                    tint = primaryText
                                )
                            }
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
           LazyColumn(
               state = lazyListState
           ) {
               when(messagesResult){
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
                                   text = "Ошибка: ${messagesResult.message}",
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
                       val messages = messagesResult.data ?: return@LazyColumn

                       items(messages){ message ->
                           Row(
                               modifier = Modifier.width(screenWidthDp),
                               horizontalArrangement = if (userId == message.from)
                                   Arrangement.End
                                else
                                    Arrangement.Start
                           ) {
                               Card(
                                   modifier = Modifier
                                       .widthIn(max = (screenWidthDp / 2))
                                       .padding(5.dp),
                                   backgroundColor = if (userId == message.from)
                                       tintMyMessageColor
                                   else
                                       secondaryBackground,
                                   shape = AbsoluteRoundedCornerShape(10.dp)
                               ) {
                                   Column {

                                       message.images.forEach { image ->
                                           com.example.messenger.ui.view.Image(
                                               url = image.url,
                                               progressbarModifier = Modifier
                                                   .fillMaxWidth()
                                                   .align(Alignment.CenterHorizontally)
                                                   .padding(15.dp),
                                               progressbarColor = if (userId == message.from)
                                                   secondaryBackground
                                               else
                                                   tintMyMessageColor,
                                               modifier = Modifier
                                                   .fillMaxWidth()
                                                   .align(Alignment.CenterHorizontally)
                                                   .padding(5.dp)
                                                   .clickable {
                                                       imageUrl = image.url
                                                       navController.navigate(
                                                           Screen.ZoomableImageScreen.arguments(
                                                               url = image.url
                                                           )
                                                       ) {
                                                           popUpTo(Screen.ChatScreen.route) {
                                                               saveState = true
                                                           }
                                                       }
                                                   }
                                           )
                                       }

                                       Text(
                                           text = message.text,
                                           color = primaryText,
                                           modifier = Modifier.padding(
                                               start = 15.dp,
                                               top = 15.dp,
                                               end = 15.dp
                                           )
                                       )

                                       Text(
                                           text = message.timeStamp,
                                           color = primaryText,
                                           fontWeight = FontWeight.W100,
                                           fontSize = 14.sp,
                                           modifier = Modifier.padding(15.dp)
                                       )
                                   }
                               }
                           }
                       }

                       item {
                           Spacer(modifier = Modifier.height(
                               if (attachFileCardVisible || images.isNotEmpty()
                           ) screenHeightDp / 4
                                else
                                    60.dp
                           ))
                       }
                   }
               }
           }
       }
    }
}