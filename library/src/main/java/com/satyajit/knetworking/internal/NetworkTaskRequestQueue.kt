package com.satyajit.knetworking.internal

import com.satyajit.knetworking.KNetworkRequest

class NetworkTaskRequestQueue(private val networkDispatcher: NetworkDispatcher) {

    private val idRequestMap: HashMap<Int, KNetworkRequest> = hashMapOf()

    fun <T> enqueue(
        request: KNetworkRequest,
        onSuccess: (response: T) -> Unit,
        onError: (error: String) -> Unit,
        converter: Converter
    ) {
        idRequestMap[request.requestID] = request
        networkDispatcher.enqueue <T> (request,onSuccess,onError,converter)
    }

}