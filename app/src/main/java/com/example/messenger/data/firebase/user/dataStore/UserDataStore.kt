package com.example.messenger.data.firebase.user.dataStore

import android.util.Log
import com.example.messenger.data.firebase.user.model.User
import com.example.messenger.utils.FirebaseConstants.CHILD_ONLINE_STATUS
import com.example.messenger.utils.FirebaseConstants.CHILD_PHOTO
import com.example.messenger.utils.FirebaseConstants.CHILD_USERNAME
import com.example.messenger.utils.FirebaseConstants.NODE_USERS
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import javax.inject.Inject

class UserDataStore @Inject constructor(
    private val database: FirebaseDatabase,
    private val auth: FirebaseAuth
) {

    fun insertUser(
        user: User,
        onCompleteListener:(Task<Void>) -> Unit = {}
    ){
        database.reference
            .child(NODE_USERS)
            .child(user.id)
            .updateChildren(user.dataMap())
            .addOnCompleteListener {
                onCompleteListener(it)
            }
    }

    fun getUserById(
        id:String = auth.currentUser?.uid.toString(),
        onSuccessListener:(User) -> Unit = {},
        onFailureListener:(Exception) -> Unit = {}
    ) {
        database.reference.child(NODE_USERS).child(id)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    onSuccessListener(
                        snapshot.getValue(User::class.java) ?: return
                    )
                }

                override fun onCancelled(error: DatabaseError) {
                    onFailureListener(error.toException())
                }
            })
    }

    fun getUsers(
        currentUser:Boolean = false,
        onSuccessListener:(List<User>) -> Unit = {},
        onFailureListener:(Exception) -> Unit = {}
    ){
        try {
            val users = mutableListOf<User>()

            database.reference.child(NODE_USERS)
                .orderByChild(CHILD_USERNAME)
                .addValueEventListener(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (i in snapshot.children){
                            i.getValue<User>()?.let { user ->
                                if (user.id == auth.currentUser?.uid && !currentUser)
                                    return@let
                                users.add(user)
                            }
                        }
                        onSuccessListener(users)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        onFailureListener(error.toException())
                    }
                })
        }catch (e:Exception){
            onFailureListener(e)
        }
    }

    fun updateUsername(
        username:String,
        onSuccessListener:() -> Unit = {},
        onFailureListener:(Exception) -> Unit = {}
    ){
        try {
            database.reference.child(NODE_USERS)
                .child(auth.currentUser?.uid ?: return)
                .child(CHILD_USERNAME)
                .setValue(username)
                .addOnSuccessListener { onSuccessListener() }
                .addOnFailureListener { onFailureListener(it) }
        }catch (e:Exception){
            onFailureListener(e)
        }
    }

    fun updatePhoto(
        onSuccessListener:() -> Unit = {},
        onFailureListener:(Exception) -> Unit = {}
    ){
        database.reference.child(NODE_USERS).child(auth.currentUser?.uid ?: return)
            .child(CHILD_PHOTO)
    }

    fun updatePhoneNumber(
        newPhoneNumber:String
    ){
        val userId = auth.currentUser?.uid ?: return

        getUserById(
            id = userId,
            onSuccessListener = { user ->

            },
            onFailureListener = { e ->
                throw Exception(e)
            }
        )
    }

    fun updateOnlineStatus(
        status:Boolean
    ){
        try {
            val id = auth.currentUser?.uid ?: return

            database.reference
                .child(NODE_USERS)
                .child(id)
                .child(CHILD_ONLINE_STATUS)
                .setValue(status)
        }catch (e:Exception){
            Log.e("UserDataStore.updateOnlineStatus()",e.message.toString())
        }
    }
}