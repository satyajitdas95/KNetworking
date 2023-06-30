package com.satyajit.knetworking.internal

import com.satyajit.knetworking.KNetworkRequest

class NetworkTaskRequestQueue(private val networkDispatcher: NetworkDispatcher) {

    private val idRequestMap: HashMap<Int, KNetworkRequest> = hashMapOf()

    fun enqueue(request: KNetworkRequest) {
        idRequestMap[request.requestID] = request
        networkDispatcher.enqueue(request)
    }

}