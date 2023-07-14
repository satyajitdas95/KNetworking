package com.example

import android.app.Application
import com.example.base.ViewModelProviderFactory
import com.example.ui.mainactivity.MainViewModel
import com.satyajit.knetworking.KNetworking
import com.satyajit.knetworking.converter_gson.GsonConverter

class MyApplication : Application() {

    lateinit var kNetworking: KNetworking

    override fun onCreate() {
        super.onCreate()

        kNetworking = KNetworking.create(applicationContext, converter = GsonConverter())

    }

    fun provideSpecificViewModelFactory(): ViewModelProviderFactory<MainViewModel> {
        return ViewModelProviderFactory(MainViewModel::class) {
            MainViewModel(kNetworking)
        }
    }


}


