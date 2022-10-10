package com.example.messenger.ui.screens.chatScreen

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.messenger.data.firebase.mainList.dataStore.MainListDataStore
import com.example.messenger.data.firebase.mainList.model.CreateMainListItem
import com.example.messenger.data.firebase.mainList.model.MainListItem
import com.example.messenger.data.firebase.message.dataStore.MessageDataStore
import com.example.messenger.data.firebase.message.model.Message
import com.example.messenger.data.firebase.user.dataStore.UserDataStore
import com.example.messenger.data.firebase.user.model.User
import com.example.messenger.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val messageDataStore: MessageDataStore,
    private val userDataStore: UserDataStore,
    private val mainListDataStore: MainListDataStore
):ViewModel() {

    private val _responseReceivingUser = MutableStateFlow<Result<User>>(Result.Loading())
    val responseReceivingUser = _responseReceivingUser.asStateFlow()

    private val _responseMessages = MutableStateFlow<Result<List<Message>>>(Result.Loading())
    val responseMessages = _responseMessages.asStateFlow()

    fun getUserById(receivingUserId:String){
        viewModelScope.launch {
            try {
                userDataStore.getUserById(
                    id = receivingUserId,
                    onSuccessListener = { user ->
                        _responseReceivingUser.value = Result.Success(user)
                    },
                    onFailureListener = { exception ->
                        _responseReceivingUser.value = Result.Error(exception.message.toString())
                    }

                )
            }catch (e:Exception){
                _responseReceivingUser.value = Result.Error(e.message.toString())
            }
        }
    }

    fun sendMessage(
        message:Message,
        receivingUserId:String,
        images:List<Uri> = emptyList(),
        bitmapImages:List<Bitmap> = emptyList(),
        onProgress: (progress:Long,totalKb:Long,transferredKb:Long) -> Unit
    ){
        viewModelScope.launch {
            messageDataStore.sendMessage(
                message = message,
                receivingUserId = receivingUserId,
                images = images,
                bitmapImages = bitmapImages,
                onProgress = onProgress
            )
        }
    }

    fun getMessages(
        receivingUserId:String
    ){
        viewModelScope.launch {
            try {
                messageDataStore.getMessages(
                    receivingUserId = receivingUserId,
                    onSuccessListener = { messages ->
                        _responseMessages.value = Result.Success(messages)
                    },
                    onFailureListener = { e ->
                        _responseMessages.value = Result.Error(e.message.toString())
                    }
                )
            }catch (e:Exception){
                _responseMessages.value = Result.Error(e.message.toString())
            }
        }
    }

    fun sendToMainList(
        mainListItem: MainListItem
    ){
        viewModelScope.launch {
            mainListDataStore.sendToMainList(
                mainListItem = mainListItem
            )
        }
    }
}