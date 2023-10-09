package com.example

import android.app.Application
import com.example.base.ViewModelProviderFactory
import com.example.ui.mainactivity.MainViewModel
import com.satyajit.knetworking.KNetworking
import com.satyajit.knetworking.KNetworkingConfig
import com.satyajit.knetworking.converter_gson.GsonConverter
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class MyApplication : Application() {

    lateinit var kNetworking: KNetworking

    override fun onCreate() {
        super.onCreate()

        val okHttpClient = OkHttpClient.Builder()
            .cache(Cache(cacheDir, 1000))
//                    .addNetworkInterceptor(StethoInterceptor())
            .addInterceptor(
                HttpLoggingInterceptor()
                    .apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
            .readTimeout(500.toLong(), TimeUnit.SECONDS)
            .writeTimeout(500.toLong(), TimeUnit.SECONDS)
            .build()

        kNetworking = KNetworking.create(
            context = applicationContext,
            kNetworkingConfig = KNetworkingConfig(okHttpClient),
            converter = GsonConverter()
        )

    }

    fun provideSpecificViewModelFactory(): ViewModelProviderFactory<MainViewModel> {
        return ViewModelProviderFactory(MainViewModel::class) {
            MainViewModel(kNetworking)
        }
    }


}


