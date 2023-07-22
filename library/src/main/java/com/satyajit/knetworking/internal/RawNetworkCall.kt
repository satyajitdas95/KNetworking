package com.satyajit.knetworking.internal

import com.satyajit.knetworking.KNetworkRequest
import com.satyajit.knetworking.RequestMethod
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.Headers
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import java.io.File


class RawNetworkCall(
    private val kNetworkRequest: KNetworkRequest,
    private val okHttpClient: OkHttpClient
) {

    companion object {
        private const val sUserAgent = "UserAgent"
    }

    private val mApplicationJsonString: String? = null
    private val mStringBody: String? = null
    private val mByte: ByteArray? = null
    private val mFile: File? = null
    private val JSON_MEDIA_TYPE: MediaType = "application/json; charset=utf-8".toMediaType()
    private val MEDIA_TYPE_MARKDOWN: MediaType = "text/x-markdown; charset=utf-8".toMediaType()
    private val customMediaType: MediaType? = null

    suspend inline fun run(
        crossinline onSuccess: (response: String) -> Unit = {},
        crossinline onError: (error: String) -> Unit = { _ -> },
    ) = run(object : KNetworkRequest.Listener {
        override fun onSuccess(response: String) = onSuccess(response)

        override fun onError(error: String) = onError(error)

    })


    suspend fun run(listener: KNetworkRequest.Listener) {
        withContext(Dispatchers.IO) {

            val url = getUrlWithAllParams(kNetworkRequest)
            var requestBuilder = Request.Builder().url(url)
            addHeadersToRequestBuilder(requestBuilder, kNetworkRequest)
            val requestBody: RequestBody?

            when (kNetworkRequest.requestType) {
                RequestMethod.Get -> {
                    requestBuilder = requestBuilder.get()
                }

                RequestMethod.Head -> {
                    requestBuilder = requestBuilder.get()
                }

                RequestMethod.Post -> {
                    requestBody = getRequestBody(kNetworkRequest)
                    requestBuilder = requestBuilder.post(requestBody)
                }

                RequestMethod.Put -> {
                    requestBody = getRequestBody(kNetworkRequest)
                    requestBuilder = requestBuilder.post(requestBody)
                }

                RequestMethod.Patch -> {
                    requestBody = getRequestBody(kNetworkRequest)
                    requestBuilder = requestBuilder.post(requestBody)
                }

                RequestMethod.Options -> {
                    requestBuilder = requestBuilder.get()
                }

                RequestMethod.Delete -> {
                    requestBody = getRequestBody(kNetworkRequest)
                    requestBuilder = requestBuilder.post(requestBody)
                }

            }

            makeNetworkCall(requestBuilder.build(), listener)
        }
    }


    fun getUrlWithAllParams(kNetworkRequest: KNetworkRequest): String {
        var tempUrl: String = kNetworkRequest.url

        kNetworkRequest.pathParametersMap?.let { pathParameterMap ->
            for ((key, value) in pathParameterMap.entries) {
                if (tempUrl.contains("{$key}")) {
                    tempUrl = tempUrl.replace("{$key}", value)
                } else {
                    throw Exception("Url doesnot have the pathParam you have provided")
                }
            }
        }

        val urlBuilder: HttpUrl.Builder = tempUrl.toHttpUrl().newBuilder()
        kNetworkRequest.queryParameterMap?.let { queryParameterMap ->
            val entries: Set<Map.Entry<String, List<String>?>> = queryParameterMap.entries
            for ((name, list) in entries) {
                list?.forEach { value ->
                    urlBuilder.addQueryParameter(name, value)
                }
            }
        }

        return urlBuilder.build().toString()
    }


    private fun addHeadersToRequestBuilder(builder: Request.Builder, request: KNetworkRequest) {
        if (request.userAgent != null) {
            builder.addHeader("UserAgent", request.userAgent!!)
        } else if (request.userAgent.isNullOrEmpty()) {
            request.userAgent = sUserAgent
            builder.addHeader("UserAgent", request.userAgent!!)
        }

        val requestHeaders: Headers = request.getHeaders()

        builder.headers(requestHeaders)

    }


    private fun getRequestBody(request: KNetworkRequest): RequestBody {
        return if (mApplicationJsonString != null) {
            if (customMediaType != null) {
                mApplicationJsonString.toRequestBody(customMediaType)
            } else {
                mApplicationJsonString.toRequestBody(JSON_MEDIA_TYPE)
            }
        } else if (mStringBody != null) {
            if (customMediaType != null) {
                mStringBody.toRequestBody(customMediaType)
            } else {
                mStringBody.toRequestBody(MEDIA_TYPE_MARKDOWN)
            }
        } else if (mFile != null) {
            if (customMediaType != null) {
                mFile.asRequestBody(customMediaType)
            } else {
                mFile.asRequestBody(MEDIA_TYPE_MARKDOWN)
            }
        } else if (mByte != null) {
            if (customMediaType != null) {
                mByte.toRequestBody(customMediaType, 0, mByte.size)
            } else {
                mByte.toRequestBody(MEDIA_TYPE_MARKDOWN, 0, mByte.size)
            }
        } else {
            val builder = FormBody.Builder()
            try {
                request.bodyParameterMap?.entries?.forEach { (key, value) ->
                    builder.add(key, value)
                }

                request.urlEncodedFormBodyParameterMap?.entries?.forEach { (key, value) ->
                    builder.add(key, value)
                }

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            builder.build()
        }
    }


    private fun makeNetworkCall(request: Request, listener: KNetworkRequest.Listener) {
        okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                listener.onError(response.message)
                throw IOException("Unexpected code $response")
            }

            listener.onSuccess(response.body?.byteString().toString())
        }
    }

}


