package com.satyajit.knetworking.internal


interface Converter {
    fun objectToString(obj: Object): String

    fun stringToObject(value: String): Object
}