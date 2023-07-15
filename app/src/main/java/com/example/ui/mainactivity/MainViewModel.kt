package com.example.ui.mainactivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.base.UiState
import com.satyajit.knetworking.KNetworking
import com.satyajit.knetworking.Priority
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.CacheControl
import java.util.concurrent.TimeUnit

class MainViewModel(var kNetworking: KNetworking) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<String>>(UiState.Loading)

    val uiState: StateFlow<UiState<String>> = _uiState.asStateFlow()

    init {
        fetchAllUsers()
        //postUser()
    }

    private fun fetchAllUsers() {
        data class TestHeader(var authorization:String,var apikey:String)
        data class TestQueryParam(var id:String)
        data class TestPathParam(var abc:String)

        val kNetworkRequest = kNetworking.newGetRequestBuilder(url = "https://reqres.in/api/users?page=2")
            .setTag("TestTag1")
            .setPriority(Priority.MEDIUM)

            .setHeaders("Test Header 1","test value 1")
            .setHeaders(mapOf(Pair("Test Header 2","test value 2")))
            .setHeaders(mutableMapOf(Pair("Test Header 3","test value 3")))
            .setHeaders(hashMapOf(Pair("Test Header 4","test value 4")))
            .setHeaders(TestHeader("Zahgr@1376n","1234"))

            .setQueryParameter("TestQueryParam1","TestQueryParam1")
            .setQueryParameter(mapOf(Pair("TestQueryParam2","TestQueryParam2")))
            .setQueryParameter(mutableMapOf(Pair("TestQueryParam3","TestQueryParam3")))
            .setQueryParameter(hashMapOf(Pair("TestQueryParam4","TestQueryParam4")))
            .setQueryParameter(TestQueryParam("id"))

            .setPathParameter("TestPathPAram1","TestPathPAram1")
            .setPathParameter(mapOf(Pair("TestPathParam2","TestPathParam2")))
            .setPathParameter(mutableMapOf(Pair("TestPathParam3","TestPathParam3")))
            .setPathParameter(hashMapOf(Pair("TestPathParam4","TestPathParam4")))
            .setPathParameter(TestPathParam("xyz"))

            .setCacheControl(CacheControl.Builder().noCache().maxAge(1,TimeUnit.DAYS).build())
            .setUserAgent("AndroidApp")

            .build()

        val knet= kNetworkRequest

        viewModelScope.launch {
            kNetworking.enqueue(kNetworkRequest, onSuccess = {
                _uiState.value = UiState.Success(it)
            }, onError = {
                _uiState.value = UiState.Error(it)
            })
        }

    }

    private fun postUser() {
//        val kNetworkRequest = kNetworking.newPostRequestBuilder(url = "https://reqres.in/api/users")
//            .tag("Tag1")
//            .headers(mapOf(Pair("Auth","3294yh")))
//            .build()
//
//        viewModelScope.launch {
//            kNetworking.enqueue(kNetworkRequest, onSuccess = {
//                _uiState.value = UiState.Success(it)
//            }, onError = {
//                _uiState.value = UiState.Error(it)
//            })
//        }

    }



}