package com.satyajit.knetworking

import android.content.Context
import com.satyajit.knetworking.internal.Converter
import com.satyajit.knetworking.internal.NetworkDispatcher
import com.satyajit.knetworking.internal.NetworkTaskRequestQueue
import com.satyajit.knetworking.internal.ParserFactory

open class KNetworking private constructor(
    context: Context,
    private val kNetworkingConfig: KNetworkingConfig,
    private val converter: ParserFactory = ParserFactory()
) {

    companion object {
        fun create(
            context: Context,
            kNetworkingConfig: KNetworkingConfig = KNetworkingConfig(), converter: Converter? = null
        ): KNetworking {
            return KNetworking(context = context, kNetworkingConfig = kNetworkingConfig)
        }
    }

    private val dispatcher = NetworkDispatcher(kNetworkingConfig.okhttpClient)
    private val reqQueue = NetworkTaskRequestQueue(dispatcher)
    fun enqueue(networkRequest: KNetworkRequest,responseClass : Any?, listener: KNetworkRequest.Listener) {
        networkRequest.listener = listener
        reqQueue.enqueue(request = networkRequest)
    }

    fun newGetRequestBuilder(
        url: String,
    ): KNetworkRequest.GetBuilder {
        return KNetworkRequest.GetBuilder(url = url, converter, RequestMethod.Get)
    }

    fun newHeadRequestBuilder(
        url: String,
    ): KNetworkRequest.HeadBuilder {
        return KNetworkRequest.HeadBuilder(url = url, converter)
    }

    fun newPostRequestBuilder(
        url: String,
    ): KNetworkRequest.PostBuilder {
        return KNetworkRequest.PostBuilder(url = url, converter)
    }

    fun newPutRequestBuilder(
        url: String,
    ): KNetworkRequest.PutRequestBuilder {
        return KNetworkRequest.PutRequestBuilder(url = url, converter)
    }

    fun newDeleteRequestBuilder(
        url: String,
    ): KNetworkRequest.DeleteRequestBuilder {
        return KNetworkRequest.DeleteRequestBuilder(url = url, converter)
    }

    fun newPatchRequestBuilder(
        url: String,
    ): KNetworkRequest.PatchRequestBuilder {
        return KNetworkRequest.PatchRequestBuilder(url = url, converter)
    }


    inline fun enqueue(
        kNetworkRequest: KNetworkRequest,
        responseClass : Any? = null,
        crossinline onSuccess: (response: Any) -> Unit = { _ -> },
        crossinline onError: (error: String) -> Unit = { _ -> },
    ) = enqueue(kNetworkRequest, responseClass, object : KNetworkRequest.Listener {
        override fun onSuccess(response: Any) = onSuccess(response)

        override fun onError(error: String) = onError(error)
    })

}