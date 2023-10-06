package com.satyajit.knetworking

import com.satyajit.knetworking.internal.Converter
import com.satyajit.knetworking.internal.KNetworkingConstant
import com.satyajit.knetworking.internal.ParserFactory
import com.satyajit.knetworking.utils.Utils
import kotlinx.coroutines.Job
import okhttp3.CacheControl
import okhttp3.Headers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import kotlin.reflect.KClass
import kotlin.system.measureTimeMillis


data class KNetworkRequest <T> private constructor(
    internal var requestType: RequestMethod,
    internal var priority: Priority,
    internal var requestID: Int,
    internal var url: String,
    internal var tag: String?,
    internal var applicationJsonString: String? = null,
    internal var stringBody: String? = null,
    internal var customContentType: String? = null,
    internal var mFile: File? = null,
    internal var headersMap: HashMap<String, MutableList<String>>? = null,
    internal var queryParameterMap: HashMap<String, MutableList<String>>? = null,
    internal var pathParametersMap: HashMap<String, String>? = null,
    internal var bodyParameterMap: HashMap<String, String>? = null,
    internal var urlEncodedFormBodyParameterMap: HashMap<String, String>? = null,
    internal var cacheControl: CacheControl? = null,
    internal var userAgent: String,
    internal var listener: Listener?,
    internal var responseClass: T?,
    internal val converter: ParserFactory<T>
) {
    internal lateinit var job: Job

    open class GetBuilder<T>(
        private val url: String,

        private val converter: ParserFactory<T>,

        private var requestType: RequestMethod = RequestMethod.Get
    ) {
        private val requestMethod: RequestMethod = requestType

        private var priority: Priority = Priority.MEDIUM

        private val requestID = Utils.getUniqueId(url)

        private val callUrl = url

        private var tag: String? = null

        private var headersMap: HashMap<String, MutableList<String>> = hashMapOf()

        private var queryParameterMap: HashMap<String, MutableList<String>> = hashMapOf()

        private var pathParametersMap: HashMap<String, String> = hashMapOf()

        private var cacheControl: CacheControl? = null

        private var userAgent: String = KNetworkingConstant.USER_AGENT_DEFAULT_VALUE

        private var listener: Listener? = null

        private var responseClass: T? = null

        fun setPriority(priority: Priority) = apply { this.priority = priority }

        fun setTag(tag: String) = apply { this.tag = tag }

        fun setHeaders(key: String, value: String): GetBuilder<T> {
            var headerList: MutableList<String>? = headersMap[key]
            if (headerList == null) {
                headerList = mutableListOf()
                headersMap[key] = headerList
            }
            if (!headerList.contains(value)) {
                headerList.add(value)
            }

            return this.apply { this.headersMap[key] = headerList }
        }


        fun setHeaders(headerMap: HashMap<String, String>): GetBuilder<T> {
            return setHeaders(headerMap.toMap())
        }

        fun setHeaders(headerMap: Map<String, String>): GetBuilder<T> {
            headerMap.run {
                for ((key, value) in this.entries) {
                    setHeaders(key = key, value = value)
                }
            }

            return this
        }

        fun setHeaders(T: Any): GetBuilder<T> {
            return this.apply {
                setHeaders(
                    converter.getStringMap(T)
                )
            }
        }

        fun setQueryParameter(key: String, value: String): GetBuilder<T> {
            var list: MutableList<String>? = queryParameterMap[key]

            if (list == null) {
                list = mutableListOf()
                queryParameterMap[key] = list
            }

            if (!list.contains(value)) {
                list.add(value)
            }

            return this@GetBuilder
        }


        fun setQueryParameter(queryParameterMap: HashMap<String, String>): GetBuilder<T> {
            return this@GetBuilder.setQueryParameter(queryParameterMap.toMap())
        }

        fun setQueryParameter(queryParameterMap: Map<String, String>): GetBuilder<T> {
            queryParameterMap.let {
                for ((key, value) in it.entries) {
                    setQueryParameter(key, value)
                }
            }
            return this@GetBuilder
        }

        fun setQueryParameter(T: Any): GetBuilder<T> {
            T.run {
                setQueryParameter(
                    converter.getStringMap(this)
                )
            }
            return this@GetBuilder
        }

        fun setPathParameter(key: String, value: String): GetBuilder<T> {
            this.pathParametersMap[key] = value
            return this@GetBuilder
        }

        fun setPathParameter(pathParameterMap: Map<String, String>): GetBuilder<T> {
            this.pathParametersMap.putAll(pathParameterMap)
            return this@GetBuilder
        }

        fun setPathParameter(pathParameterMap: HashMap<String, String>): GetBuilder<T> {
            return this@GetBuilder.setPathParameter(pathParameterMap.toMap())
        }


        fun setPathParameter(T: Any): GetBuilder<T> {
            T.apply {
                setPathParameter(
                    converter.getStringMap(this)
                )
            }

            return this@GetBuilder
        }

        fun setCacheControl(cacheControl: CacheControl) =
            apply { this@GetBuilder.cacheControl = cacheControl }

        fun setUserAgent(userAgent: String) = apply { this@GetBuilder.userAgent = userAgent }


        fun build(): KNetworkRequest<T> {
            return KNetworkRequest(
                requestType = requestMethod,
                priority = priority,
                requestID = requestID,
                url = callUrl,
                tag = tag,
                headersMap = headersMap,
                queryParameterMap = queryParameterMap,
                pathParametersMap = pathParametersMap,
                cacheControl = cacheControl,
                userAgent = userAgent,
                listener = listener,
                responseClass = responseClass,
                converter = converter
            )

        }
    }

    class HeadBuilder<T>(
        url: String,
        converter: ParserFactory<T>,
        requestType: RequestMethod = RequestMethod.Head
    ) : GetBuilder<T>(url = url, converter = converter, requestType = requestType)

    open class PostBuilder<T>(
        url: String,
        private val converter: ParserFactory<T>,
        requestType: RequestMethod = RequestMethod.Post
    ) {
        private val requestMethod: RequestMethod = requestType

        private var priority: Priority = Priority.MEDIUM

        private val requestID = Utils.getUniqueId(url)

        private val callUrl = url

        private var tag: String? = null

        private var applicationJsonString: String? = null

        private var stringBody: String? = null

        private var customContentType: String? = null

        private var file: File? = null

        private var headersMap: HashMap<String, MutableList<String>> = hashMapOf()

        private var queryParameterMap: HashMap<String, MutableList<String>> = hashMapOf()

        private var pathParametersMap: HashMap<String, String> = hashMapOf()

        private var bodyParameterMap: HashMap<String, String> = hashMapOf()

        private var urlEncodedFormBodyParameterMap: HashMap<String, String> = hashMapOf()

        private var cacheControl: CacheControl? = null

        private var userAgent: String = KNetworkingConstant.USER_AGENT_DEFAULT_VALUE

        private var listener: Listener? = null

        private var responseClass = null

        fun setPriority(priority: Priority) = apply { this.priority = priority }

        fun setTag(tag: String) = apply { this.tag = tag }

        fun setHeaders(key: String, value: String): PostBuilder<T> {
            var headerList: MutableList<String>? = headersMap[key]
            if (headerList == null) {
                headerList = mutableListOf()
                headersMap[key] = headerList
            }
            if (!headerList.contains(value)) {
                headerList.add(value)
            }

            return this.apply { this.headersMap[key] = headerList }
        }


        fun setHeaders(headerMap: HashMap<String, String>): PostBuilder<T> {
            return setHeaders(headerMap.toMap())
        }

        fun setHeaders(headerMap: Map<String, String>): PostBuilder<T> {
            headerMap.run {
                for ((key, value) in this.entries) {
                    setHeaders(key = key, value = value)
                }
            }

            return this
        }

        fun setHeaders(T: Any): PostBuilder<T> {
            return this.apply {
                setHeaders(
                    converter.getStringMap(T)
                )
            }
        }

        fun setQueryParameter(key: String, value: String, isEncoded: Boolean = false): PostBuilder<T> {
            var list: MutableList<String>? = queryParameterMap[key]

            if (list == null) {
                list = mutableListOf()
                queryParameterMap[key] = list
            }

            if (!list.contains(value) && !list.contains(
                    Utils.encodeValue(
                        value = value,
                        isEncoded = isEncoded
                    )
                )
            ) {
                list.add(Utils.encodeValue(value, isEncoded))
            }

            return this@PostBuilder
        }


        fun setQueryParameter(
            queryParameterMap: HashMap<String, String>,
            isEncoded: Boolean = false
        ): PostBuilder<T> {
            return this@PostBuilder.setQueryParameter(
                queryParameterMap = queryParameterMap.toMap(),
                isEncoded = isEncoded
            )
        }

        fun setQueryParameter(
            queryParameterMap: Map<String, String>,
            isEncoded: Boolean = false
        ): PostBuilder<T> {
            queryParameterMap.let {
                for ((key, value) in it.entries) {
                    setQueryParameter(key = key, value = value, isEncoded = isEncoded)
                }
            }
            return this@PostBuilder
        }

        fun setQueryParameter(T: Any, isEncoded: Boolean = false): PostBuilder<T> {
            T.run {
                setQueryParameter(
                    converter.getStringMap(this), isEncoded
                )
            }
            return this@PostBuilder
        }

        fun setPathParameter(key: String, value: String, isEncoded: Boolean = false): PostBuilder<T> {
            this.pathParametersMap[key] = Utils.encodeValue(value, isEncoded)
            return this@PostBuilder
        }

        fun setPathParameter(
            pathParameterMap: Map<String, String>,
            isEncoded: Boolean = false
        ): PostBuilder<T> {
            val mutablePathParamMap = pathParameterMap.toMutableMap()
            mutablePathParamMap.forEach { it ->
                mutablePathParamMap[it.key] = Utils.encodeValue(it.value, isEncoded)
            }
            this.pathParametersMap.putAll(mutablePathParamMap)
            return this@PostBuilder
        }

        fun setPathParameter(
            pathParameterMap: HashMap<String, String>,
            isEncoded: Boolean = false
        ): PostBuilder<T> {
            return this@PostBuilder.setPathParameter(
                pathParameterMap = pathParameterMap.toMap(),
                isEncoded = isEncoded
            )
        }


        fun setPathParameter(T: Any, isEncoded: Boolean = false): PostBuilder<T> {
            T.apply {
                setPathParameter(
                    pathParameterMap = converter.getStringMap(this), isEncoded = isEncoded
                )
            }

            return this@PostBuilder
        }

        fun setCacheControl(cacheControl: CacheControl) =
            apply { this@PostBuilder.cacheControl = cacheControl }

        fun setUserAgent(userAgent: String) = apply { this@PostBuilder.userAgent = userAgent }


        fun setBodyParameter(key: String, value: String): PostBuilder<T> {
            bodyParameterMap[key] = value
            return this@PostBuilder
        }

        fun setBodyParameter(bodyParameterMap: Map<String, String>): PostBuilder<T> {
            this.bodyParameterMap.putAll(bodyParameterMap)
            return this@PostBuilder
        }

        fun setBodyParameter(T: Any?): PostBuilder<T> {
            T?.let {
                bodyParameterMap.putAll(
                    converter.getStringMap(it)
                )
            }
            return this@PostBuilder
        }

        fun setUrlEncodeFormBodyParameter(key: String, value: String): PostBuilder<T> {
            urlEncodedFormBodyParameterMap[key] = value
            return this@PostBuilder
        }

        fun setUrlEncodeFormBodyParameter(bodyParameterMap: Map<String, String>): PostBuilder<T> {
            urlEncodedFormBodyParameterMap.putAll(bodyParameterMap)
            return this@PostBuilder
        }

        fun setUrlEncodeFormBodyParameter(T: Any?): PostBuilder<T> {
            T?.let {
                urlEncodedFormBodyParameterMap.putAll(converter.getStringMap(it))
            }
            return this@PostBuilder
        }

        fun setApplicationJsonBody(T: Any?): PostBuilder<T> {
            T?.let {
                applicationJsonString = converter.getString(it)
            }
            return this@PostBuilder
        }

        fun setJSONObjectBody(jsonObject: JSONObject): PostBuilder<T> {
            applicationJsonString = jsonObject.toString()
            return this@PostBuilder
        }

        fun setJSONArrayBody(jsonArray: JSONArray?): PostBuilder<T> {
            if (jsonArray != null) {
                applicationJsonString = jsonArray.toString()
            }
            return this@PostBuilder
        }

        fun setStringBody(stringBody: String): PostBuilder<T> {
            this.stringBody = stringBody
            return this@PostBuilder
        }

        fun setFileBody(file: File): PostBuilder<T> {
            this.file = file
            return this@PostBuilder
        }

        fun setContentType(contentType: String): PostBuilder<T> {
            customContentType = contentType
            return this@PostBuilder
        }


        fun build(): KNetworkRequest<T> {
            return KNetworkRequest(
                requestType = requestMethod,
                priority = priority,
                requestID = requestID,
                url = callUrl,
                tag = tag,
                applicationJsonString = applicationJsonString,
                stringBody = stringBody,
                customContentType = customContentType?.toMediaTypeOrNull().toString(),
                mFile = file,
                headersMap = headersMap,
                bodyParameterMap = bodyParameterMap,
                urlEncodedFormBodyParameterMap = urlEncodedFormBodyParameterMap,
                queryParameterMap = queryParameterMap,
                pathParametersMap = pathParametersMap,
                cacheControl = cacheControl,
                userAgent = userAgent,
                listener = listener,
                responseClass = responseClass,
                converter = ParserFactory(),
            )
        }
    }

    class PutRequestBuilder<T>(
        url: String,
        converter: ParserFactory<T>,
        requestType: RequestMethod = RequestMethod.Put
    ) : PostBuilder<T>(url, converter, requestType)

    class DeleteRequestBuilder<T>(
        url: String,
        converter: ParserFactory<T>,
        requestType: RequestMethod = RequestMethod.Delete
    ) : PostBuilder<T>(url, converter, requestType)

    class PatchRequestBuilder<T>(
        url: String,
        converter: ParserFactory<T>,
        requestType: RequestMethod = RequestMethod.Patch
    ) : PostBuilder<T>(url, converter, requestType)


    fun getHeaders(): Headers {
        val builder = Headers.Builder()
        try {
            headersMap?.let {
                val entries: Set<Map.Entry<String, List<String>?>> = it.entries
                for ((name, list) in entries) {
                    if (list != null) {
                        for (value in list) {
                            builder.add(name, value)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return builder.build()
    }

    interface Listener {
        fun <T> onSuccess(responseClass: T)

        fun onError(error: String)
    }

}


sealed class RequestMethod {
    object Get : RequestMethod()
    object Post : RequestMethod()
    object Head : RequestMethod()
    object Put : RequestMethod()
    object Delete : RequestMethod()
    object Options : RequestMethod()
    object Patch : RequestMethod()
}

