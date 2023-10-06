package com.satyajit.knetworking.internal

import com.satyajit.knetworking.KNetworkRequest

class NetworkTaskRequestQueue<T>(private val networkDispatcher: NetworkDispatcher<T>) {

    private val idRequestMap: HashMap<Int, KNetworkRequest<T>> = hashMapOf()

    fun enqueue(request: KNetworkRequest<T>) {
        idRequestMap[request.requestID] = request
        networkDispatcher.enqueue(request)
    }

}