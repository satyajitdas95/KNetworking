package com.satyajit.knetworking.internal

import com.satyajit.knetworking.KNetworkRequest
import com.satyajit.knetworking.dispacther.DispatcherProviderImpl
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

class NetworkDispatcher(private val okHttpClient: OkHttpClient) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main +
            CoroutineExceptionHandler { _, _ ->

            })

    private val dispatchers = DispatcherProviderImpl()

    fun <T> enqueue(
        req: KNetworkRequest,
        onSuccess: (response: T) -> Unit,
        onError: (error: String) -> Unit,
        converter: Converter
    ) {
        val job = scope.launch {
            execute<T>(req, onSuccess, onError,converter)
        }
        req.job = job
    }


    private suspend fun <T> execute(
        request: KNetworkRequest,
        onSuccess: (response: T) -> Unit,
        onError: (error: String) -> Unit,
        converter: Converter
    ) {

        NetworkTask(request, okHttpClient, dispatchers).run<T>(
            scope,
            onSuccess,
            onError,converter
        )
    }

     fun executeOnMainThread(block: () -> Unit) {
        scope.launch {
            block()
        }
    }


}