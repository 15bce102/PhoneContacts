package com.example.phonecontacts

import android.R
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView



class ContactsAdapter(context: Context?, contacts: ArrayList<Contact>?) :
    ArrayAdapter<Contact?>(context!!, 0, contacts!! as List<Contact?>) {




    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View { // Get the data item
        val contact: Contact? = getItem(position)

        var view: View? = convertView
        if (view == null) {
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.simple_list_item_2, parent, false)
        }

        val tvName = view?.findViewById(R.id.text1) as TextView
        val tvPhone = view.findViewById(R.id.text2) as TextView
        if (contact != null) {
            tvName.text = contact.name
        }

        tvPhone.text = ""

        if (contact != null) {
            if (contact.numbers.size > 0 && contact.numbers.get(0) != null) {
                var locale = context.resources.configuration.locale.country
                Log.d("country","country code +${locale}")
                tvPhone.text = contact.numbers[0].number
            }
        }
        return view
    }


}