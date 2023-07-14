package com.satyajit.knetworking.internal

import com.satyajit.knetworking.KNetworkRequest
import com.satyajit.knetworking.RequestMethod
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

            val request = Request.Builder()
            request.url(kNetworkRequest.url)

            when (kNetworkRequest.requestType) {
                RequestMethod.Get -> {
                    addHeader(request, kNetworkRequest.headers)
                }

                RequestMethod.Head -> {
                    addHeader(request, kNetworkRequest.headers)
                }

                RequestMethod.Post -> {
                    addHeader(request, kNetworkRequest.headers)
                    addParameters(request, kNetworkRequest.parameters)
                }

                RequestMethod.Put -> {
                    addHeader(request, kNetworkRequest.headers)
                    if (!kNetworkRequest.parameters.isNullOrEmpty()) addParameters(
                        request,
                        kNetworkRequest.parameters
                    )
                }

                RequestMethod.Delete -> {
                    addHeader(request, kNetworkRequest.headers)
                    addParameters(request, kNetworkRequest.parameters)
                }

                RequestMethod.Patch -> {
                    addHeader(request, kNetworkRequest.headers)
                    addParameters(request, kNetworkRequest.parameters)
                }

            }
            makeNetworkCall(request.build(), listener)
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

    private fun makeNetworkCall(request: Request, listener: KNetworkRequest.Listener) {
        okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                listener.onError(response.message)
                throw IOException("Unexpected code $response")
            }

            listener.onSuccess("")
        }
    }

}


