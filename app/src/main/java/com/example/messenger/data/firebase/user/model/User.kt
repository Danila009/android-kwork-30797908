package com.example.messenger.data.firebase.user.model

import com.example.messenger.utils.FirebaseConstants.CHILD_ID
import com.example.messenger.utils.FirebaseConstants.CHILD_PHONE
import com.example.messenger.utils.FirebaseConstants.CHILD_PHOTO
import com.example.messenger.utils.FirebaseConstants.CHILD_USERNAME

data class User(
    val id:String = "",
    val username:String? = null,
    val phone:String = "",
    val onlineStatus:Boolean = false,
    val photo:String? = null
){
    fun dataMap(): MutableMap<String, Any> {
        val map = mutableMapOf<String,Any>()

        map[CHILD_ID] = id
        map[CHILD_PHONE] = phone

        username?.let {
            map[CHILD_USERNAME] = username
        }

        photo?.let {
            map[CHILD_PHOTO] = photo
        }

        return map
    }
}