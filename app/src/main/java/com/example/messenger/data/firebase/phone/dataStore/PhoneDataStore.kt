package com.example.messenger.data.firebase.phone.dataStore

import android.util.Log
import com.example.messenger.utils.FirebaseConstants.NODE_PHONES
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class PhoneDataStore @Inject constructor(
    private val database: FirebaseDatabase,
    private val auth: FirebaseAuth
) {
    fun addPhone(
        phone:String,
        onSuccessListener:() -> Unit,
        onFailureListener:() -> Unit
    ){
        database.reference.child(NODE_PHONES)
            .child(phone)
            .setValue(auth.currentUser?.uid)
            .addOnSuccessListener { onSuccessListener() }
            .addOnFailureListener { onFailureListener() }
    }

    fun getPhones(
        onSuccessListener:(uid:List<String>) -> Unit = {},
        onFailureListener:(error: Exception) -> Unit = {}
    ){
        database.reference.child(NODE_PHONES).get()
            .addOnSuccessListener { onSuccessListener(it.children.map { it.toString() }) }
            .addOnFailureListener { onFailureListener(it) }
    }

    fun getPhone(
        phoneNumber:String,
        onSuccessListener:(userId: String?) -> Unit = {},
        onFailureListener:(error: Exception) -> Unit = {}
    ){
        database.reference.child(NODE_PHONES).child(phoneNumber).get()
            .addOnSuccessListener { onSuccessListener(
                if (it.value == null) null
            else
                it.value.toString()
            ) }
            .addOnFailureListener { onFailureListener(it) }
    }
}