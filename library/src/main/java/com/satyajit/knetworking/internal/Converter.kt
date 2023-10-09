package com.satyajit.knetworking.internal


interface Converter {

    fun <T> objectToString(obj: T): String

    fun <T> stringToObject(value: String): T

    fun getStringMap(t: Any): Map<String, String>

    fun getString(it: Any): String

}