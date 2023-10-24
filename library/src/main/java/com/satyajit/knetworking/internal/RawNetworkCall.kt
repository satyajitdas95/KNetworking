package com.satyajit.knetworking.internal

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException

class RawNetworkCall(
    private val okHttpClient: OkHttpClient,
    private val req: Request,
    private val converter: Converter,
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main +
            CoroutineExceptionHandler { _, _ ->

            })

    fun <T> makeNetworkCall(
        onSuccess: (T) -> Unit, onError: (error: String) -> Unit
    ) {
        okHttpClient.newCall(req).execute().use { response ->
            if (!response.isSuccessful) {
                executeOnMainThread { onError.invoke(response.message) }
                throw IOException("Unexpected code $response")
            }

            response.body!!.string().let { responseJson ->
                val responseConverted = converter.stringToObject<T>(responseJson)
                val res =responseConverted as T
                executeOnMainThread { onSuccess.invoke(responseConverted)}
            }

        }
    }

    private fun executeOnMainThread(block: () -> Unit) {
        scope.launch {
            block()
        }
    }

}