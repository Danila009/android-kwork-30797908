package com.example.messenger.utils

object FirebaseConstants {

    //Firestore
    const val COLLECTION_MAIN_LIST = "main_list"
    const val COLLECTION_USERS = "users"

    //Realtime database

    const val NODE_USERS = "users"
    const val NODE_PHONES = "phones"
    const val NODE_PHONES_CONTACTS = "phones_contacts"
    const val NODE_MESSAGES = "messages"
    const val NODE_IMAGES = "images"
    const val NODE_MAIN_LIST = "main_list"

    //Store
    const val FOLDER_MESSAGES = "messages"
    const val FOLDER_IMAGES = "images"

    //User
    const val CHILD_ID = "id"
    const val CHILD_USERNAME = "username"
    const val CHILD_PHOTO = "photo"
    const val CHILD_PHONE = "phone"
    const val CHILD_LAST_MESSAGE = "lastMessage"

    //Message
    const val CHILD_TEXT = "text"
    const val CHILD_TYPE = "type"
    const val CHILD_FROM = "from"
    const val CHILD_TIME_STAMP = "timeStamp"
    const val CHILD_DATE_TIME = "dateTime"

    //Image
    const val CHILD_URL = "url"

}