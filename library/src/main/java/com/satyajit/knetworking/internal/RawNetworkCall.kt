package com.satyajit.knetworking.internal

import com.satyajit.knetworking.KNetworkRequest
import com.satyajit.knetworking.RequestType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException

class RawNetworkCall(
    private val kNetworkRequest: KNetworkRequest,
    private val okHttpClient: OkHttpClient
) {

    suspend inline fun run(
        crossinline onSuccess: (response: String) -> Unit = {},
        crossinline onError: (error: String) -> Unit = { _ -> },
    ) = run(object : KNetworkRequest.Listener {
        override fun onSuccess(response: String) = onSuccess(response)

        override fun onError(error: String) = onError(error)

    })


    suspend fun run(listener: KNetworkRequest.Listener) {
        withContext(Dispatchers.IO) {

            when (kNetworkRequest.requestType) {
                RequestType.Get -> {
                    val request = Request.Builder()
                    addHeader(request, kNetworkRequest.headers)
                    request.url(kNetworkRequest.url)
                    makeNetworkCall(request.build(), listener)
                }

                RequestType.Post -> {
                    val request = Request.Builder()
                    addHeader(request, kNetworkRequest.headers)
                    addParameters(request, kNetworkRequest.parameters)
                    request.url(kNetworkRequest.url)
                    makeNetworkCall(request.build(), listener)
                }

                RequestType.Put -> {
                    val request = Request.Builder()
                        .url(kNetworkRequest.url)
                        .build()
                    makeNetworkCall(request, listener)
                }

                RequestType.Delete -> {
                    val request = Request.Builder()
                        .url(kNetworkRequest.url)
                        .build()
                    makeNetworkCall(request, listener)
                }

                RequestType.Patch -> {
                    val request = Request.Builder()
                        .url(kNetworkRequest.url)
                        .build()
                    makeNetworkCall(request, listener)
                }

                RequestType.Head -> {
                    val request = Request.Builder()
                        .url(kNetworkRequest.url)
                        .build()
                    makeNetworkCall(request, listener)
                }


                RequestType.Options -> {
                    val request = Request.Builder()
                        .url(kNetworkRequest.url)
                        .build()
                    makeNetworkCall(request, listener)
                }
            }

        }
    }



    private fun addHeader(request: Request.Builder, headers: Map<String, String>?) {
        headers?.let { headers ->
            for ((key, value) in headers) {
                request.addHeader(key, value)
            }
        }

    }

    private fun addParameters(request: Request.Builder, parameters: List<Pair<String, String>>?) {
        if (parameters.isNullOrEmpty()) {

        } else {
            val formBody = FormBody.Builder()
            parameters.forEach { param ->
                formBody.add(param.first, param.second)
            }
            request.post(formBody.build())
        }
    }

    fun makeNetworkCall(request: Request, listener: KNetworkRequest.Listener) {

        okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            listener.onSuccess(response.body?.byteString().toString())
        }
    }

}