@file:OptIn(ExperimentalSerializationApi::class)

package com.example.messenger.utils.extensions

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

inline fun<reified T> T.encodeToString():String{
    val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }
    return json.encodeToString(this)
}

inline fun<reified T> String.decodeFromString():T{
    val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }
    return json.decodeFromString(this)
}