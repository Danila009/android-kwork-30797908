package com.example.messenger.data.network.messagingService.repository

import android.util.Log
import com.example.messenger.data.network.messagingService.api.MessagingServiceApi
import com.example.messenger.data.network.messagingService.model.PushMessagingBody
import javax.inject.Inject

class MessagingServiceRepository @Inject constructor(
    private val api:MessagingServiceApi
) {

    suspend fun pushMessaging(body: PushMessagingBody){
        val response = api.pushMessaging(body = body)
        Log.e("pushMessaging",response.code().toString())
        Log.e("pushMessaging",response.message().toString())
        Log.e("pushMessaging",response.body().toString())
    }
}