package com.example.messenger.utils.extensions

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

fun Bitmap.asByteArray():ByteArray {
    val stream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 90, stream)
    return stream.toByteArray()
}