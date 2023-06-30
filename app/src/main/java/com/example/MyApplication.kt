package com.example

import android.app.Application
import com.example.base.ViewModelProviderFactory
import com.example.ui.mainactivity.MainViewModel
import com.satyajit.knetworking.KNetworking

class MyApplication : Application() {

    lateinit var kNetworking: KNetworking

    override fun onCreate() {
        super.onCreate()

        kNetworking = KNetworking.create(applicationContext)

    }

    fun provideSpecificViewModelFactory(): ViewModelProviderFactory<MainViewModel> {
        return ViewModelProviderFactory(MainViewModel::class) {
            MainViewModel(kNetworking)
        }
    }


}


