package com.example.phonecontacts

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract

import android.provider.ContactsContract.CommonDataKinds.Email

import android.provider.ContactsContract.CommonDataKinds.Phone
import androidx.loader.content.CursorLoader


class ContactFetcher(c: Context) {
    private val context: Context = c
    fun fetchAll(): ArrayList<Contact> {
        val projectionFields = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME
        )
        val listContacts: ArrayList<Contact> = ArrayList()
        val cursorLoader = CursorLoader(
            context,
            ContactsContract.Contacts.CONTENT_URI,
            projectionFields,  // the columns to retrieve
            null,  // the selection criteria (none)
            null,  // the selection args (none)
            null // the sort order (default)
        )
        val c: Cursor? = cursorLoader.loadInBackground()
        val contactsMap: HashMap<String, Contact>? =
            c?.count?.let { HashMap(it) }
        if (c != null) {
            if (c.moveToFirst()) {
                val idIndex: Int = c.getColumnIndex(ContactsContract.Contacts._ID)
                val nameIndex: Int = c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                if (c != null) {
                    do {
                        val contactId: String = c.getString(idIndex)
                        val contactDisplayName: String = c.getString(nameIndex)
                        val contact = Contact(contactId, contactDisplayName)
                        contactsMap?.set(contactId, contact)
                        listContacts.add(contact)
                    } while (c.moveToNext())
                }
            }
        }
        c?.close()
        if (contactsMap != null) {
            matchContactNumbers(contactsMap)
        }

        return listContacts
    }

    private fun matchContactNumbers(contactsMap: Map<String, Contact>) { // Get numbers
        val numberProjection = arrayOf(
            Phone.NUMBER,
            Phone.TYPE,
            Phone.CONTACT_ID
        )
        val phone: Cursor? = CursorLoader(
            context,
            Phone.CONTENT_URI,
            numberProjection,
            null,
            null,
            null
        ).loadInBackground()
        if (phone != null) {
            if (phone.moveToFirst()) {
                val contactNumberColumnIndex: Int = phone.getColumnIndex(Phone.NUMBER)
                val contactTypeColumnIndex: Int = phone.getColumnIndex(Phone.TYPE)
                val contactIdColumnIndex: Int = phone.getColumnIndex(Phone.CONTACT_ID)
                while (!phone.isAfterLast) {
                    val number: String = phone.getString(contactNumberColumnIndex)
                    val contactId: String = phone.getString(contactIdColumnIndex)
                    val contact: Contact = contactsMap[contactId] ?: continue
                    val type: Int = phone.getInt(contactTypeColumnIndex)
                    val customLabel = "Custom"
                    val phoneType =
                        Phone.getTypeLabel(
                            context.resources,
                            type,
                            customLabel
                        )
                    contact.addNumber(number, phoneType.toString())
                    phone.moveToNext()
                }
            }
        }
        phone?.close()
    }




}