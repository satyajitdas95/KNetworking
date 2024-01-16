package com.satyajit.knetworking

import android.content.Context
import com.satyajit.knetworking.internal.Converter
import com.satyajit.knetworking.internal.NetworkDispatcher
import com.satyajit.knetworking.internal.NetworkTaskRequestQueue
import kotlin.reflect.KClass

open class KNetworking private constructor(
    context: Context,
    private val kNetworkingConfig: KNetworkingConfig,
    private val converter: Converter
) {

    companion object {
        fun create(
            context: Context,
            kNetworkingConfig: KNetworkingConfig = KNetworkingConfig(), converter: Converter
        ): KNetworking {
            return KNetworking(
                context = context,
                kNetworkingConfig = kNetworkingConfig,
                converter = converter
            )
        }
    }

    private val dispatcher = NetworkDispatcher(kNetworkingConfig.okhttpClient)

    private val reqQueue = NetworkTaskRequestQueue(dispatcher)


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

    fun getClassType(clazz: Any) {
        val classtype = clazz.javaClass
    }


//    inline fun enqueue(
//        kNetworkRequest: KNetworkRequest<T>,
//        responseClass: T,
//        crossinline onSuccess: (response: T) -> Unit = { _ -> },
//        crossinline onError: (error: String) -> Unit = { _ -> },
//    ) = enqueue(kNetworkRequest, responseClass, object : KNetworkRequest.Listener {
//        override fun <T> onSuccess(responseClass: T) = onSuccess(responseClass)
//        override fun onError(error: String) = onError(error)
//    })

     inline fun <reified T> enqueue(
        networkRequest: KNetworkRequest,
        noinline onSuccess: (T) -> Unit = { _ -> },
        noinline onError: (error: String) -> Unit = { _ -> }
    ) {
         reqQueue.enqueue<T>(request = networkRequest, onSuccess, onError, converter = converter)
    }


}