package com.satyajit.knetworking.internal

import com.satyajit.knetworking.KNetworkRequest
import com.satyajit.knetworking.dispacther.DispatcherProviderImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException


class NetworkTask(
    private val kNetworkRequest: KNetworkRequest,
    private val okHttpClient: OkHttpClient,
    private val dispatchers: DispatcherProviderImpl
) {


    suspend fun <T> run(
        scope: CoroutineScope,
        onSuccess: (response: T) -> Unit,
        onError: (error: String) -> Unit,
        converter: Converter
    ) {
        withContext(dispatchers.getDispatcherIO()) {

            val okhttpRequestBuilder = OkhttpRequestBuilder(kNetworkRequest)
            val request : Request = okhttpRequestBuilder.getRequest()

            val rawNetworkCall = RawNetworkCall(
                okHttpClient = okHttpClient,
                req = request,
                converter = converter,
            )

            rawNetworkCall.makeNetworkCall<T>(onSuccess = onSuccess, onError = onError)

        }
    }


}


