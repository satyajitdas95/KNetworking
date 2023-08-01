package com.satyajit.knetworking.dispacther

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {
    fun getDispatcherMain () :CoroutineDispatcher
    fun getDispatcherIO () :CoroutineDispatcher
    fun getDispatcherDefault () :CoroutineDispatcher
}