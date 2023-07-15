package com.satyajit.knetworking.internal

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.Exception
import java.lang.reflect.Type


class ParserFactory {

    private val gson = Gson()

    fun getStringMap(T: Any): Map<String, String> {
        try {
            val type: Type = object : TypeToken<HashMap<String, String>>() {}.type
            return gson.fromJson(gson.toJson(T), type)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyMap()
    }
}