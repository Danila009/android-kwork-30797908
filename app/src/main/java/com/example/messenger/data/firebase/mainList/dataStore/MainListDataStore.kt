package com.example.messenger.data.firebase.mainList.dataStore

import com.example.messenger.data.firebase.mainList.model.MainListItem
import com.example.messenger.data.firebase.user.dataStore.UserDataStore
import com.example.messenger.utils.FirebaseConstants.COLLECTION_MAIN_LIST
import com.example.messenger.utils.FirebaseConstants.COLLECTION_USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import javax.inject.Inject

class MainListDataStore @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val userDataStore: UserDataStore,
) {

    fun sendToMainList(
        mainListItem: MainListItem,
        onSuccessListener:() -> Unit = {},
        onFailureListener:(Exception) -> Unit = {}
    ){
        val userId = auth.currentUser?.uid ?: return

        userDataStore.getUserById(
            id = userId,
            onSuccessListener = { user ->
                val newMainListItem = MainListItem(
                    id = userId,
                    username = user.username,
                    phone = user.phone,
                    lastMessage = mainListItem.lastMessage,
                    dateTime = mainListItem.dateTime
                )

                firestore.collection(COLLECTION_USERS)
                    .document(mainListItem.id)
                    .collection(COLLECTION_MAIN_LIST)
                    .document(userId)
                    .set(newMainListItem)
                    .addOnSuccessListener {

                        firestore.collection(COLLECTION_USERS)
                            .document(userId)
                            .collection(COLLECTION_MAIN_LIST)
                            .document(mainListItem.id)
                            .set(mainListItem)
                            .addOnSuccessListener {
                                onSuccessListener()
                            }
                            .addOnFailureListener { onFailureListener(it) }
                    }
                    .addOnFailureListener { onFailureListener(it) }
            }
        )
    }

    fun getMainList(
        onSuccessListener:(List<MainListItem>) -> Unit = {},
        onFailureListener:(Exception) -> Unit = {}
    ){
        val id = auth.currentUser?.uid ?: return

        val docRef = firestore
            .collection(COLLECTION_USERS)
            .document(id)
            .collection(COLLECTION_MAIN_LIST)

        docRef.addSnapshotListener() { snapshot, e ->

            if (e != null) {
                onFailureListener(e)
            }

            if (snapshot == null || snapshot.isEmpty){
                onSuccessListener(emptyList())
            }

            if (snapshot != null && snapshot.any { it.exists() }) {
                onSuccessListener(snapshot.map { it.toObject() })
            }
        }
    }
}