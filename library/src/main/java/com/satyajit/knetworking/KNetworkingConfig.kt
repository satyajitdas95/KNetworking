package com.satyajit.knetworking

import okhttp3.OkHttpClient

class KNetworkingConfig(var okhttpClient: OkHttpClient = OkHttpClient(), var baseUrl: String)