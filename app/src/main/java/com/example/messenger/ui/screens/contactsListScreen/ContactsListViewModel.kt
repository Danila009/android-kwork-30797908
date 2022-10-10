package com.example.messenger.ui.screens.contactsListScreen

import androidx.lifecycle.ViewModel
import com.example.messenger.data.database.contacts.dataStore.ContactsDataStore
import com.example.messenger.data.firebase.phone.dataStore.PhoneDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactsListViewModel @Inject constructor(
    contactsDataStore: ContactsDataStore,
    private val phoneDataStore: PhoneDataStore
):ViewModel() {

    val getContacts = contactsDataStore.getContacts()

    fun getPhone(
        phoneNumber:String,
        onSuccessListener:(String?) -> Unit,
        onFailureListener:() -> Unit
    ){
        var correctionPhoneNumber = phoneNumber
            .replace(Regex("[\\s,-]"),"")
            .replace("+","")
            .replace("(","")
            .replace(")","")

        if (correctionPhoneNumber.startsWith("8")){
            correctionPhoneNumber = "7" + correctionPhoneNumber.removeRange(0,1)
        }

        phoneDataStore.getPhone(
            phoneNumber = " $correctionPhoneNumber",
            onSuccessListener = {
                onSuccessListener(it)
            },
            onFailureListener = {onFailureListener()}
        )
    }
}