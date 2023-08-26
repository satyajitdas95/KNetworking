package com.satyajit.knetworking.internal

import com.satyajit.knetworking.KNetworkRequest
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException

class RawNetworkCall(
    private var okHttpClient: OkHttpClient,
    private var req: Request,
    private var responseType: Any?,
    private var converter: ParserFactory,
    private var listener: KNetworkRequest.Listener
) {

    fun makeNetworkCall(
    ) {
        okHttpClient.newCall(req).execute().use { response ->
            if (!response.isSuccessful) {
                listener.onError(response.message)
                throw IOException("Unexpected code $response")
            }

            if (responseType != null){
                converter.getStringMap()
            }else{
                listener.onSuccess(response.body?.byteString().toString())
            }

        }
    }

}