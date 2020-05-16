package com.example.phonecontacts

import android.Manifest
import android.R
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.widget.ListView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.blongho.country_data.Country
import com.blongho.country_data.World
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import java.util.*


class MainActivity : Activity() {

    var listContacts: ArrayList<Contact>? = null
    var lvContacts: ListView? = null
    private var util: PhoneNumberUtil? = null
    private var country: Country? = null
    private var entered: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_content)
        World.init(applicationContext);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED) {
            
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                
            } else {
                
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_CONTACTS),
                    0)

              
            }
        } else {
            // Permission has already been granted

            getCountryCode()
            listContacts = ContactFetcher(this).fetchAll()
            lvContacts = findViewById<View>(R.id.list) as ListView
            val adapterContacts = ContactsAdapter(this, listContacts)
            lvContacts!!.adapter = adapterContacts
        }
        

    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            0 -> {

                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    private fun getCountryCode(){
        val tm =
            this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val countryCode = (tm.simCountryIso.toUpperCase(Locale.US)).trim()
        Log.d("hello","country +${countryCode}")



        if (util == null) {
            util = PhoneNumberUtil.createInstance(applicationContext)

        }


        val phoneNumber = util!!.parse("9461066067", "IN")
        var check = util!!.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
        Log.d("hello","country code +${check}")
    }

}

