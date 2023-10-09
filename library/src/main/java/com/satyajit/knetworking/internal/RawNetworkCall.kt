package com.satyajit.knetworking.internal

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException

class RawNetworkCall(
    private val okHttpClient: OkHttpClient,
    private val req: Request,
    private val converter: Converter,
    private val scope: CoroutineScope
) {

    fun <T> makeNetworkCall(
        onSuccess: (T) -> Unit, onError: (error: String) -> Unit
    ) {
        okHttpClient.newCall(req).execute().use { response ->
            if (!response.isSuccessful) {
                executeOnMainThread { onError.invoke(response.message) }
                throw IOException("Unexpected code $response")
            }

            val response = converter.stringToObject<T>(response.body?.byteString().toString())

            executeOnMainThread { onSuccess.invoke(response) }

        }
    }

    private fun executeOnMainThread(block: () -> Unit) {
        scope.launch {
            block()
        }
    }

}