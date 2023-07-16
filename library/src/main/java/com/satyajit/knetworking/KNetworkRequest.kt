package com.satyajit.knetworking

import com.satyajit.knetworking.internal.ParserFactory
import com.satyajit.knetworking.utils.getUniqueId
import kotlinx.coroutines.Job
import okhttp3.CacheControl

typealias Parameters = List<Pair<String, String>>


class KNetworkRequest private constructor(
    internal var requestType: RequestMethod,
    internal var priority: Priority,
    internal var requestID: Int,
    internal var url: String,
    internal var tag: String?,
    internal var headers: HashMap<String, MutableList<String>>? = null,
    internal var queryParameterMap: HashMap<String, MutableList<String>>? = null,
    internal var pathParametersMap: HashMap<String, String>? = null,
    internal var cacheControl: CacheControl? = null,
    internal var userAgent: String? = null,
    internal var listener: Listener?,
) {
    internal lateinit var job: Job


    data class GetBuilder(private val url: String, val converter: ParserFactory) {
        private val requestMethod: RequestMethod = RequestMethod.Get

        private var priority: Priority = Priority.MEDIUM

        private val requestID = getUniqueId(url)

        private val callUrl = url

        private var tag: String? = null

        private var headersMap: HashMap<String, MutableList<String>> = hashMapOf()

        private var queryParameterMap: HashMap<String, MutableList<String>> = hashMapOf()

        private var pathParametersMap: HashMap<String, String> = hashMapOf()

        private var cacheControl: CacheControl? = null

        private var userAgent: String? = null

        private var listener: Listener? = null

        fun setPriority(priority: Priority) = apply { this.priority = priority }

        fun setTag(tag: String) = apply { this.tag = tag }

        fun setHeaders(key: String, value: String): GetBuilder {
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


        fun setHeaders(headerMap: HashMap<String, String>): GetBuilder {
            return setHeaders(headerMap.toMap())
        }

        fun setHeaders(headerMap: Map<String, String>): GetBuilder {
            headerMap.run {
                for ((key, value) in this.entries) {
                    setHeaders(key = key, value = value)
                }
            }

            return this
        }

        fun setHeaders(T: Any): GetBuilder {
            return this.apply {
                setHeaders(
                    converter.getStringMap(T)
                )
            }
        }

        fun setQueryParameter(key: String, value: String): GetBuilder {
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


        fun setQueryParameter(queryParameterMap: HashMap<String, String>): GetBuilder {
            return this@GetBuilder.setQueryParameter(queryParameterMap.toMap())
        }

        fun setQueryParameter(queryParameterMap: Map<String, String>): GetBuilder {
            queryParameterMap.let {
                for ((key, value) in it.entries) {
                    setQueryParameter(key, value)
                }
            }
            return this@GetBuilder
        }

        fun setQueryParameter(T: Any): GetBuilder {
            T.run {
                setQueryParameter(
                    converter.getStringMap(this)
                )
            }
            return this@GetBuilder
        }

        fun setPathParameter(key: String, value: String): GetBuilder {
            this.pathParametersMap[key] = value
            return this@GetBuilder
        }

        fun setPathParameter(pathParameterMap: Map<String, String>): GetBuilder {
            this.pathParametersMap.putAll(pathParameterMap)
            return this@GetBuilder
        }

        fun setPathParameter(pathParameterMap: HashMap<String, String>): GetBuilder {
            return this@GetBuilder.setPathParameter(pathParameterMap.toMap())
        }


        fun setPathParameter(T: Any): GetBuilder {
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


        fun build(): KNetworkRequest {
            return KNetworkRequest(
                requestType = requestMethod,
                priority = priority,
                requestID = requestID,
                url = callUrl,
                tag = tag,
                headers = headersMap,
                queryParameterMap = queryParameterMap,
                pathParametersMap = pathParametersMap,
                cacheControl = cacheControl,
                userAgent = userAgent,
                listener = listener
            )

        }
    }

    data class PostBuilder(private val url: String, val converter: ParserFactory) {
        private val requestMethod: RequestMethod = RequestMethod.Post

        private var priority: Priority = Priority.MEDIUM

        private val requestID = getUniqueId(url)

        private val callUrl = url

        private var tag: String? = null

        private var headersMap: HashMap<String, MutableList<String>> = hashMapOf()

        private var queryParameterMap: HashMap<String, MutableList<String>> = hashMapOf()

        private var pathParametersMap: HashMap<String, String> = hashMapOf()

        private var cacheControl: CacheControl? = null

        private var userAgent: String? = null

        private var listener: Listener? = null

        fun setPriority(priority: Priority) = apply { this.priority = priority }

        fun setTag(tag: String) = apply { this.tag = tag }

        fun setHeaders(key: String, value: String): PostBuilder {
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


        fun setHeaders(headerMap: HashMap<String, String>): PostBuilder {
            return setHeaders(headerMap.toMap())
        }

        fun setHeaders(headerMap: Map<String, String>): PostBuilder {
            headerMap.run {
                for ((key, value) in this.entries) {
                    setHeaders(key = key, value = value)
                }
            }

            return this
        }

        fun setHeaders(T: Any): PostBuilder {
            return this.apply {
                setHeaders(
                    converter.getStringMap(T)
                )
            }
        }

        fun setQueryParameter(key: String, value: String): PostBuilder {
            var list: MutableList<String>? = queryParameterMap[key]

            if (list == null) {
                list = mutableListOf()
                queryParameterMap[key] = list
            }

            if (!list.contains(value)) {
                list.add(value)
            }

            return this@PostBuilder
        }


        fun setQueryParameter(queryParameterMap: HashMap<String, String>): PostBuilder {
            return this@PostBuilder.setQueryParameter(queryParameterMap.toMap())
        }

        fun setQueryParameter(queryParameterMap: Map<String, String>): PostBuilder {
            queryParameterMap.let {
                for ((key, value) in it.entries) {
                    setQueryParameter(key, value)
                }
            }
            return this@PostBuilder
        }

        fun setQueryParameter(T: Any): PostBuilder {
            T.run {
                setQueryParameter(
                    converter.getStringMap(this)
                )
            }
            return this@PostBuilder
        }

        fun setPathParameter(key: String, value: String): PostBuilder {
            this.pathParametersMap[key] = value
            return this@PostBuilder
        }

        fun setPathParameter(pathParameterMap: Map<String, String>): PostBuilder {
            this.pathParametersMap.putAll(pathParameterMap)
            return this@PostBuilder
        }

        fun setPathParameter(pathParameterMap: HashMap<String, String>): PostBuilder {
            return this@PostBuilder.setPathParameter(pathParameterMap.toMap())
        }


        fun setPathParameter(T: Any): PostBuilder {
            T.apply {
                setPathParameter(
                    converter.getStringMap(this)
                )
            }

            return this@PostBuilder
        }

        fun setCacheControl(cacheControl: CacheControl) =
            apply { this@PostBuilder.cacheControl = cacheControl }

        fun setUserAgent(userAgent: String) = apply { this@PostBuilder.userAgent = userAgent }


        fun build(): KNetworkRequest {
            return KNetworkRequest(
                requestType = requestMethod,
                priority = priority,
                requestID = requestID,
                url = callUrl,
                tag = tag,
                headers = headersMap,
                queryParameterMap = queryParameterMap,
                pathParametersMap = pathParametersMap,
                cacheControl = cacheControl,
                userAgent = userAgent,
                listener = listener
            )

        }
    }

    interface Listener {
        fun onSuccess(response: String)

        fun onError(error: String)
    }

}


sealed class RequestMethod {
    object Get : RequestMethod()
    object Post : RequestMethod()
    object Put : RequestMethod()
    object Delete : RequestMethod()
    object Patch : RequestMethod()
    object Head : RequestMethod()
}

