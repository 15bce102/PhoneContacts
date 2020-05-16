package com.example.phonecontacts

import java.util.*


class Contact(var id: String, var name: String) {

    var numbers: ArrayList<ContactPhone> = ArrayList<ContactPhone>()
    override fun toString(): String {
        var result = name
        if (numbers.size > 0) {
            val number: ContactPhone = numbers[0]
            result += " (" + number.number + " - " + number.type + ")"
        }

        return result
    }



    fun addNumber(number: String?, type: String?) {
        if(type!=null&&number!=null)
        numbers.add(ContactPhone(number, type))
    }

}