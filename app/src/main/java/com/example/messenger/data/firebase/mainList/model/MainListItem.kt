package com.example.messenger.data.firebase.mainList.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class CreateMainListItem(
    val id:String
)

data class MainListItem(
    var id:String = "",
    var phone:String = "",
    var username:String? = "",
    var lastMessage:String = "",
    @ServerTimestamp
    var dateTime: Date? = null
)