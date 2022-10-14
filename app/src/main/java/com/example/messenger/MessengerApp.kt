package com.example.messenger

import android.app.Application
import android.content.Context
import android.util.Log
import com.example.messenger.data.firebase.user.dataStore.UserDataStore
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MessengerApp:Application() {

    @Inject
    lateinit var firebaseMessaging: FirebaseMessaging

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onCreate() {
        super.onCreate()
        firebaseAppCheck()
        firebaseMessagingToken()
        subscribeTopic()
    }

    private fun subscribeTopic(){
        auth.currentUser?.uid?.let { uid ->
            val topic = "/topics/$uid"
            Log.e("TOPIC",topic)
            firebaseMessaging.subscribeToTopic(topic)
        }
    }

    private fun firebaseMessagingToken(){
        MainFirebaseMessagingService.sharedPref = getSharedPreferences(
            MainFirebaseMessagingService.TOKEN_SHARED_KEY,
            Context.MODE_PRIVATE
        )

        FirebaseInstallations.getInstance().getToken(true).addOnSuccessListener  {
            MainFirebaseMessagingService.token = it.token
        }
    }

    private fun firebaseAppCheck(){
        FirebaseApp.initializeApp(this)
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance()
        )
    }
}