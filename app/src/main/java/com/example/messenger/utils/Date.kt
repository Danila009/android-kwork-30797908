package com.example.messenger.utils

import android.annotation.SuppressLint
import android.icu.util.Calendar
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("NewApi")
fun getLocalDate(): String {
    val time = Calendar.getInstance().time
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    return formatter.format(time)
}