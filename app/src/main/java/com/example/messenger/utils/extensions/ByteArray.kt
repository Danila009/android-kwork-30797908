package com.example.messenger.utils.extensions

import android.graphics.Bitmap
import android.graphics.BitmapFactory

fun ByteArray.asBitmap():Bitmap {
    return BitmapFactory.decodeByteArray(this,0,this.size)
}