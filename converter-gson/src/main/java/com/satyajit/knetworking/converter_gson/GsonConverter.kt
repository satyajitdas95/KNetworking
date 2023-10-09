package com.satyajit.knetworking.converter_gson

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.satyajit.knetworking.internal.Converter
import java.lang.reflect.Type

class GsonConverter : Converter {

    private val gson = Gson()

    override fun <T> objectToString(obj: T): String {
        val type = object : TypeToken<T>() {}.type
        return gson.toJson(obj, type)
    }

    override fun <T> stringToObject(json: String): T {
        val type = object : TypeToken<T>() {}.type
        return gson.fromJson(json, type)
    }

    override fun getStringMap(T: Any): Map<String, String> {
        try {
            val type: Type = object : TypeToken<HashMap<String, String>>() {}.type
            return gson.fromJson(gson.toJson(T), type)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyMap()
    }

    override fun getString(it: Any): String {
        return ""
    }


}

