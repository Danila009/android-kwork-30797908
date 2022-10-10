package com.example.messenger.data.firebase.auth.dataSource

import android.app.Activity
import com.example.messenger.utils.FirebaseConstants
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import java.lang.Deprecated
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthDataSource @Inject constructor(
    private val auth: FirebaseAuth,
    private val database: FirebaseDatabase,
) {
    fun sendMessageAuthPhone(
        phone: String,
        activity: Activity,
        onVerificationCompleted: (PhoneAuthCredential) -> Unit = {},
        onVerificationFailed: (FirebaseException) -> Unit = {},
        onCodeSent: (otp: String, resendingToken: PhoneAuthProvider.ForceResendingToken) -> Unit = { _, _ -> },
    ){
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object :
                PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    onVerificationCompleted(p0)
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    onVerificationFailed(p0)
                }

                override fun onCodeSent(otp: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    onCodeSent(otp,p1)
                }
            }).build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verificationPhoneCode(
        verificationCode: String,
        code: String,
        onCompleteListener: (isSuccessful: Boolean) -> Unit = {},
    ){
        val credential = PhoneAuthProvider.getCredential(verificationCode, code)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                onCompleteListener(task.isSuccessful)
            }
    }

    fun signOut(){
        auth.signOut()
    }

    // not completed
    @Deprecated
    fun updatePhone(
        newPhoneNumber: String,
        oldPhoneNumber: String,
        code: String,
        onSuccessListener: () -> Unit = {},
        onFailureListener: (message: String) -> Unit = {},
    ){
        val phoneAuthCredential = PhoneAuthProvider.getCredential(
            newPhoneNumber,
            code
        )

        val currentUser = auth.currentUser ?: return

        currentUser.updatePhoneNumber(phoneAuthCredential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    //update phone in database
                    // update phone number
                    database.reference.child(FirebaseConstants.NODE_PHONES)
                        .child(oldPhoneNumber)
                        .setPriority(newPhoneNumber)
                        .addOnSuccessListener {
                            // update phone number
                            database.reference.child(FirebaseConstants.NODE_USERS)
                                .child(currentUser.uid)
                                .child(FirebaseConstants.CHILD_PHONE)
                                .setValue(newPhoneNumber)
                                .addOnSuccessListener {
                                    onSuccessListener()
                                }
                                .addOnFailureListener { e ->
                                    throw Exception(e)
                                }
                        }
                        .addOnFailureListener { e ->
                            throw Exception(e)
                        }

                } else{
                    onFailureListener(task.exception?.message.toString())
                }
            }
    }
}