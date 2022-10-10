package com.example.messenger.data.database.contacts.dataStore

import android.annotation.SuppressLint
import android.content.Context
import android.provider.ContactsContract
import com.example.messenger.data.database.contacts.model.Contact
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ContactsDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    @SuppressLint("Range")
    fun getContacts():List<Contact> {
        val contacts = ArrayList<Contact>()

        val cursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,null,null,"DISPLAY_NAME ASC"
        )

        cursor?.let {
            while (cursor.moveToNext()){
                val name = it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val phone = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                contacts.add(Contact(
                    name = name,
                    phone = phone
                ))
            }
        }

        cursor?.close()

        return contacts
    }
}