package com.satyajit.knetworking

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
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
    internal var decodeConfig: Bitmap.Config?,
    internal var decodeOptions: BitmapFactory.Options?,
    internal var maxWidth: Int?,
    internal var maxHeight: Int?,
    internal var scaleType: ImageView.ScaleType?,
    internal var headers: HashMap<String, List<String>>? = HashMap(),
    internal var queryParameterMap: HashMap<String, List<String>>? = HashMap(),
    internal var pathParametersMap: HashMap<String, String>? = HashMap(),
    internal var cacheControl: CacheControl? = null,
    internal var userAgent: String? = null,
    internal var listener: Listener?,
) {
    internal lateinit var job: Job


    data class GetBuilder(private val url: String) {
        /*Pre Defined Methods*/
        private val requestMethod: RequestMethod = RequestMethod.Get
        private var priority: Priority? = Priority.MEDIUM
        private val requestID = getUniqueId(url)

        private val callUrl = url

        /*User Define values*/

        private var tag: String? = null

        private var decodeConfig: Bitmap.Config? = null

        private var decodeOptions: BitmapFactory.Options? = null

        private var maxWidth: Int? = null

        private var maxHeight: Int? = null

        private var scaleType: ImageView.ScaleType? = null

        private var headers: HashMap<String, List<String>>? = null

        private var queryParameterMap: HashMap<String, List<String>>? = null

        private var pathParametersMap: HashMap<String, String>? = null

        private var cacheControl: CacheControl? = null

        private var userAgent: String? = null

        private var listener: Listener? = null

        fun setPriority(priority: Priority) = apply { this.priority = priority }
        fun setTag(tag: String) = apply { this.tag = tag }
        fun setDecodeConfig(decodeConfig: Bitmap.Config) =
            apply { this.decodeConfig = decodeConfig }

        fun setDecodeOptions(decodeOptions: BitmapFactory.Options) =
            apply { this.decodeOptions = decodeOptions }

        fun setMaxWidht(maxWidth: Int) = apply { this.maxWidth = maxWidth }
        fun setMaxHeight(maxHeight: Int) = apply { this.maxHeight = maxHeight }
        fun setScaleType(scaleType: ImageView.ScaleType) = apply { this.scaleType = scaleType }
        fun setHeaders(headers: HashMap<String, List<String>>) = apply { this.headers = headers }
        fun setQueryParametersMap(queryParametersMap: HashMap<String, List<String>>) =
            apply { this.queryParameterMap = queryParametersMap }
        fun setPathParametersMap(pathParametersMap: HashMap<String, String>) = apply { this.pathParametersMap = pathParametersMap }
        fun setCacheControl(cacheControl: CacheControl) = apply { this.cacheControl = cacheControl }
        fun setUserAgent(userAgent: String) = apply { this.userAgent = userAgent }


        fun build(): KNetworkRequest {
            return KNetworkRequest(
                requestType = requestMethod,
                priority = priority!!,
                requestID = requestID,
                url = callUrl,
                tag = tag,
                decodeConfig = decodeConfig,
                decodeOptions = decodeOptions,
                maxWidth = maxWidth,
                maxHeight = maxHeight,
                scaleType = scaleType,
                headers = headers,
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

