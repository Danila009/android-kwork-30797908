package com.example.messenger.utils

import android.Manifest
import android.telephony.SmsManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@ExperimentalPermissionsApi
@Composable
fun sendSms(
    message:String,
    phoneNumber:String
):Boolean {
    val smsPermission = rememberMultiplePermissionsState(permissions = listOf(
        Manifest.permission.SEND_SMS,
        Manifest.permission.READ_PHONE_STATE
    ))

    LaunchedEffect(key1 = Unit, block = {
        smsPermission.launchMultiplePermissionRequest()
    })

    if (smsPermission.allPermissionsGranted){
        val smsManager = SmsManager.getDefault()

        smsManager.sendTextMessage(
            phoneNumber,
            null,
            message,
            null,
            null
        )

        return true
    }

    return false
}