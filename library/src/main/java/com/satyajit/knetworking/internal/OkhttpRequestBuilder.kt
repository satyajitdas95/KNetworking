package com.satyajit.knetworking.internal

import com.satyajit.knetworking.KNetworkRequest
import com.satyajit.knetworking.RequestMethod
import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class OkhttpRequestBuilder(private val kNetworkRequest: KNetworkRequest) {

    private val _applicationJsonString: String? = null
    private val _stringBody: String? = null
    private val _byte: ByteArray? = null
    private val _file: File? = null
    private val _jsonMediaType: MediaType = "application/json; charset=utf-8".toMediaType()
    private val _mediaTypeMarkDown: MediaType = "text/x-markdown; charset=utf-8".toMediaType()
    private val _customMediaType: MediaType? = null

    fun getRequest(): Request {

        val url = getUrlWithAllParams(kNetworkRequest)
        var reqBuilder = Request.Builder().url(url)
        addHeadersToRequestBuilder(reqBuilder, kNetworkRequest)

        when (kNetworkRequest.requestType) {
            RequestMethod.Get -> {
                reqBuilder = reqBuilder.get()
            }

            RequestMethod.Head -> {
                reqBuilder = reqBuilder.get()
            }

            RequestMethod.Post -> {
                val requestBody = getRequestBody(kNetworkRequest)
                reqBuilder = reqBuilder.post(requestBody)
            }

            RequestMethod.Put -> {
                val requestBody = getRequestBody(kNetworkRequest)
                reqBuilder = reqBuilder.post(requestBody)
            }

            RequestMethod.Patch -> {
                val requestBody = getRequestBody(kNetworkRequest)
                reqBuilder = reqBuilder.post(requestBody)
            }

            RequestMethod.Options -> {
                reqBuilder = reqBuilder.get()
            }

            RequestMethod.Delete -> {
                val requestBody = getRequestBody(kNetworkRequest)
                reqBuilder = reqBuilder.post(requestBody)
            }

        }

        return reqBuilder.build()
    }

    private fun getUrlWithAllParams(kNetworkRequest: KNetworkRequest): String {
        var tempUrl: String = kNetworkRequest.url

        kNetworkRequest.pathParametersMap?.let { pathParameterMap ->
            for ((key, value) in pathParameterMap.entries) {
                if (tempUrl.contains("{$key}")) {
                    tempUrl = tempUrl.replace("{$key}", value)
                } else {
                    throw NoSuchFieldException("Url doesn't have the path param you have provided")
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
        builder.addHeader(KNetworkingConstant.USER_AGENT, request.userAgent)
        builder.headers(request.getHeaders())
    }

    private fun getRequestBody(request: KNetworkRequest): RequestBody {
        return if (_applicationJsonString != null) {
            if (_customMediaType != null) {
                _applicationJsonString.toRequestBody(_customMediaType)
            } else {
                _applicationJsonString.toRequestBody(_jsonMediaType)
            }
        } else if (_stringBody != null) {
            if (_customMediaType != null) {
                _stringBody.toRequestBody(_customMediaType)
            } else {
                _stringBody.toRequestBody(_mediaTypeMarkDown)
            }
        } else if (_file != null) {
            if (_customMediaType != null) {
                _file.asRequestBody(_customMediaType)
            } else {
                _file.asRequestBody(_mediaTypeMarkDown)
            }
        } else if (_byte != null) {
            if (_customMediaType != null) {
                _byte.toRequestBody(_customMediaType, 0, _byte.size)
            } else {
                _byte.toRequestBody(_mediaTypeMarkDown, 0, _byte.size)
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
}