package com.satyajit.knetworking

import com.satyajit.knetworking.utils.getUniqueId
import kotlinx.coroutines.Job

typealias Parameters = List<Pair<String, String>>

class KNetworkRequest private constructor(
    internal var requestID: Int,
    internal var url: String,
    internal var headers: Map<String, String>? = emptyMap(),
    internal var parameters: Parameters? = emptyList(),
    var requestType: RequestType,
    internal var tag: String?,
    internal var listener: Listener?
) {

    //todo doubt regarding header and param.. hashmap for same keys and list allow duplication

    internal lateinit var job: Job

    data class GetBuilder(private val url: String) {

        private var tag: String? = null

        private var headers: Map<String, String>? = null

        private var listener: Listener? = null
        fun tag(tag: String) = apply { this.tag = tag }
        fun headers(headers: Map<String, String>) = apply { this.headers = headers }
        fun build(): KNetworkRequest {
            return KNetworkRequest(
                requestID = getUniqueId(url),
                url = url,
                requestType = RequestType.Get,
                headers = headers,
                tag = tag,
                listener = listener
            )
        }
    }

    data class PostBuilder(private val url: String) {

        private var tag: String? = null

        private var headers: Map<String, String>? = null

        private var parameters: Parameters? = null

        private var listener: Listener? = null
        fun tag(tag: String) = apply { this.tag = tag }
        fun headers(headers: Map<String, String>) = apply { this.headers = headers }
        fun parameters(parameters: Parameters) = apply { this.parameters = parameters }
        fun build(): KNetworkRequest {
            return KNetworkRequest(
                requestID = getUniqueId(url),
                url,
                requestType = RequestType.Post,
                headers = headers,
                parameters = checkNotNull(parameters) { "Parameters cannot be null in post request" },
                tag = tag,
                listener = listener
            )
        }
    }


    interface Listener {
        fun onSuccess(response: String)

        fun onError(error: String)
    }

}


sealed class RequestType {
    object Get : RequestType()
    object Post : RequestType()
    object Put : RequestType()
    object Delete : RequestType()
    object Patch : RequestType()
    object Head : RequestType()
    object Options : RequestType()
}