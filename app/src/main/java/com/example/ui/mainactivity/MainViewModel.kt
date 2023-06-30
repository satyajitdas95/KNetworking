package com.example.ui.mainactivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.base.UiState
import com.satyajit.knetworking.KNetworkRequest
import com.satyajit.knetworking.KNetworking
import com.satyajit.knetworking.Parameters
import com.satyajit.knetworking.RequestType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(var kNetworking: KNetworking) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<String>>(UiState.Loading)

    val uiState: StateFlow<UiState<String>> = _uiState.asStateFlow()

    init {
//        fetchAllUsers()
        postUser()
    }

    private fun fetchAllUsers() {
        val kNetworkRequest = kNetworking.newGetRequestBuilder(url = "https://reqres.in/api/users?page=2")
            .tag("Tag1")
            .headers(mapOf(Pair("Auth","3294yh")))
            .build()

        viewModelScope.launch {
            kNetworking.enqueue(kNetworkRequest, onSuccess = {
                _uiState.value = UiState.Success(it)
            }, onError = {
                _uiState.value = UiState.Error(it)
            })
        }

    }

    private fun postUser() {
        val kNetworkRequest = kNetworking.newPostRequestBuilder(url = "https://reqres.in/api/users")
            .tag("Tag1")
            .parameters(listOf(Pair("name","Satyajit Das"),Pair("job","Software Engineer")))
            .headers(mapOf(Pair("Auth","3294yh")))
            .build()

        viewModelScope.launch {
            kNetworking.enqueue(kNetworkRequest, onSuccess = {
                _uiState.value = UiState.Success(it)
            }, onError = {
                _uiState.value = UiState.Error(it)
            })
        }

    }


}