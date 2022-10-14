package com.example.messenger.data.network.messagingService.model

data class PushMessagingBody(
    val data:PushMessagingInfo,
    val to:String
)

data class PushMessagingInfo(
    val title:String,
    val message:String
)