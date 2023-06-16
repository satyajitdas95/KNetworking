package com.satyajit.knetworking

import android.content.Context

open class KNetworking private constructor(context: Context, kNetworkingConfig: KNetworkingConfig) {

    companion object {
        fun create(context: Context, kNetworkingConfig: KNetworkingConfig): KNetworking {
            return KNetworking(context = context, kNetworkingConfig = kNetworkingConfig)
        }
    }


}