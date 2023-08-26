package com.satyajit.knetworking.internal

import com.satyajit.knetworking.KNetworkRequest
import com.satyajit.knetworking.dispacther.DispatcherProviderImpl
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

class NetworkDispatcher(private val okHttpClient: OkHttpClient){

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main +
            CoroutineExceptionHandler { _, _ ->

            })

    private val  dispatchers = DispatcherProviderImpl()

    fun enqueue(req: KNetworkRequest) {
        val job = scope.launch {
            execute(req)
        }
        req.job = job
    }

    private suspend fun execute(request: KNetworkRequest) {
        NetworkTask(request,okHttpClient,dispatchers).run(
            onSuccess = {
                executeOnMainThread { request.listener?.onSuccess(it) }
            },
            onError = {
                executeOnMainThread { request.listener?.onError(it) }
            }
        )
    }

    private fun executeOnMainThread(block: () -> Unit) {
        scope.launch {
            block()
        }
    }


}