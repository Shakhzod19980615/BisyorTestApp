package com.example.testapp.presentation.profile.viewModel

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.database.Cursor
import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.common.Resource
import com.example.testapp.domain.model.ContactModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class InviteContactsViewModel @Inject constructor(

): ViewModel() {

    private val _contacts = MutableStateFlow<List<ContactModel>>(emptyList())
    val contacts: StateFlow<List<ContactModel>> get() = _contacts
    private var originalContacts: List<ContactModel> = emptyList()
    fun fetchContacts(contentResolver: ContentResolver) {
        viewModelScope.launch {
            val contactsList = withContext(Dispatchers.IO) {
                fetchContactsFromDevice(contentResolver)
            }
            originalContacts = contactsList.distinctBy { it.phoneNumber }
            _contacts.value = contactsList
        }
    }
    @SuppressLint("Range")
    private fun fetchContactsFromDevice(contentResolver: ContentResolver): List<ContactModel> {
        val contactsList = mutableListOf<ContactModel>()
        val phoneNumberSet = mutableSetOf<String>() // Track unique phone numbers

        val cursor: Cursor? = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getString(it.getColumnIndex(ContactsContract.Contacts._ID))
                val name = it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

                if (it.getInt(it.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    val phoneCursor: Cursor? = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )

                    phoneCursor?.use { pc ->
                        while (pc.moveToNext()) {
                            val phoneNumber = pc.getString(
                                pc.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                            )
                            // Check if the phone number is already added
                            if (phoneNumberSet.add(phoneNumber)) {
                                contactsList.add(ContactModel(name, phoneNumber))
                            }
                        }
                    }
                }
            }
        }

        return contactsList
    }
    fun filterContacts(query: String) {
        val filteredList = if (query.isEmpty()) {
            originalContacts // Return the full list if the query is empty
        } else {
            originalContacts.filter { contact ->
                contact.name.contains(query, ignoreCase = true) || contact.phoneNumber.contains(query, ignoreCase = true)
            }
        }
        _contacts.value = filteredList
    }
}