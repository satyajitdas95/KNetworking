package com.satyajit.knetworking.dispacther

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


class DispatcherProviderImpl : DispatcherProvider {
    override fun getDispatcherMain(): CoroutineDispatcher {
        return Dispatchers.Main
    }

    override fun getDispatcherIO(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    override fun getDispatcherDefault(): CoroutineDispatcher {
        return Dispatchers.Default
    }

}

