package com.example.messenger.utils.extensions

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.asTime():String {
    val time = Date(this.toLong())
    return time.asTimeFormat()
}

fun String.asTimeFormat():String{
    if (this.isEmpty()) return this
    if (this == "null") return this
    val time = Date(this.toLong())
    val timeFormat = SimpleDateFormat("HH:mm dd MMMM",Locale.getDefault())
    return timeFormat.format(time)
}

fun Date.asTimeFormat():String{
    val timeFormat = SimpleDateFormat("HH:mm dd MMMM",Locale.getDefault())
    return timeFormat.format(this)
}