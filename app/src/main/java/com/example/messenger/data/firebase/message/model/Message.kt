package com.example.messenger.data.firebase.message.model

enum class MessageType {
    TEXT
}

data class Message(
    val text:String,
    val type:String,
    val from:String = "",
    val timeStamp:String = "",
    val images:List<Image> = emptyList()
)

@kotlinx.serialization.Serializable
data class Image(
    val url:String
)