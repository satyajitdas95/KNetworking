package com.satyajit.knetworking

import android.content.Context
import com.satyajit.knetworking.internal.NetworkDispatcher
import com.satyajit.knetworking.internal.NetworkTaskRequestQueue
import okhttp3.OkHttpClient

open class KNetworking private constructor(
    context: Context,
    private val kNetworkingConfig: KNetworkingConfig
) {

    companion object {
        fun create(
            context: Context,
            kNetworkingConfig: KNetworkingConfig = KNetworkingConfig()
        ): KNetworking {
            return KNetworking(context = context, kNetworkingConfig = kNetworkingConfig)
        }
    }


    private val dispatcher = NetworkDispatcher(kNetworkingConfig.okhttpClient)
    private val reqQueue = NetworkTaskRequestQueue(dispatcher)
    fun enqueue(networkRequest: KNetworkRequest, listener: KNetworkRequest.Listener) {
        networkRequest.listener = listener
        reqQueue.enqueue(request = networkRequest)
    }

    fun newGetRequestBuilder(
        url: String,
    ): KNetworkRequest.GetBuilder {
        return KNetworkRequest.GetBuilder(url = url)
    }

    fun newPostRequestBuilder(url: String): KNetworkRequest.PostBuilder {
        return KNetworkRequest.PostBuilder(url = url)
    }


    inline fun enqueue(
        kNetworkRequest: KNetworkRequest,
        crossinline onSuccess: (response: String) -> Unit = { _ -> },
        crossinline onError: (error: String) -> Unit = { _ -> },
    ) = enqueue(kNetworkRequest, object : KNetworkRequest.Listener {
        override fun onSuccess(response: String) = onSuccess(response)

        override fun onError(error: String) = onError(error)
    })

}