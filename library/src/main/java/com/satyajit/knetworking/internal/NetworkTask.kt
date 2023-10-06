package com.satyajit.knetworking.internal

import com.satyajit.knetworking.KNetworkRequest
import com.satyajit.knetworking.RequestMethod
import com.satyajit.knetworking.dispacther.DispatcherProviderImpl
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException
import kotlin.reflect.KClass


class NetworkTask<T>(
    private val kNetworkRequest: KNetworkRequest<T>,
    private val okHttpClient: OkHttpClient,
    private val dispatchers: DispatcherProviderImpl
) {


    suspend  inline fun run(
        crossinline onSuccess: (response: T) -> Unit = {},
        crossinline onError: (error: String) -> Unit = { _ -> },
    ) = run(object : KNetworkRequest.Listener {
        override fun <T>  onSuccess(response: T) = onSuccess(response)

        override fun onError(error: String) = onError(error)

    })

    suspend fun run(listener: KNetworkRequest.Listener) {
        withContext(dispatchers.getDispatcherIO()) {

            val okhttpRequestBuilder = OkhttpRequestBuilder(kNetworkRequest)
            val request = okhttpRequestBuilder.getRequest()

            okHttpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    listener.onError(response.message)
                    throw IOException("Unexpected code $response")
                }

                if (kNetworkRequest.responseClass != null) {
                    kNetworkRequest.converter.convertJsonToObj(
                        response,
                        kNetworkRequest.responseClass
                    )
                }
            }

//            val rawNetworkCall = RawNetworkCall(
//                okHttpClient = okHttpClient,
//                req = request,
//                responseType = kNetworkRequest.responseClass,
//                converter = kNetworkRequest.converter,
//                listener = listener
//            )
//
//            rawNetworkCall.makeNetworkCall()
        }
    }


}


