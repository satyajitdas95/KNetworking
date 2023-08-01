# KNetworking - Android Networking Library (WIP)

### About KNetworking Library

KNetworking Library is a powerful library for doing any type of networking in Android applications which is made on top of [OkHttp Networking Layer](http://square.github.io/okhttp/).

Just make a request and listen for the response.


### Making a GET Request
```kotlin
val kNetworkRequestGet = kNetworking.newGetRequestBuilder(url = "https://abcexample.in/api/users/{userID}/orders/{orderID}")
            .setTag("TestTag1")
            .setPriority(Priority.MEDIUM)

            .setHeaders("Header1","HeaderValue1")
            .setHeaders(mapOf(Pair("Header2","HeaderValue2")))
            .setHeaders(mutableMapOf(Pair("Header3","HeaderValue3")))
            .setHeaders(hashMapOf(Pair("Header4","HeaderValue4")))
            .setHeaders(TestHeader("Zahgr@1376n","1234"))

            .setQueryParameter("page","2")
            .setQueryParameter(mapOf(Pair("QueryParam2","QueryParamValue2")))
            .setQueryParameter(mutableMapOf(Pair("QueryParam3","QueryParamValue3")))
            .setQueryParameter(hashMapOf(Pair("QueryParam4","QueryParamValue4")))
            .setQueryParameter(TestQueryParam("QueryParamValue5"))

            .setPathParameter("userID","abc12")
            .setPathParameter("orderId","1242")
            .setPathParameter(mapOf(Pair("PathParam2","PathParamValue2")))
            .setPathParameter(mutableMapOf(Pair("PathParam3","PathParamValue3")))
            .setPathParameter(hashMapOf(Pair("PathParam4","PathParamValue4")))
            .setPathParameter(TestPathParam("PathParamValue5"))

            .setCacheControl(CacheControl.Builder().noCache().maxAge(1,TimeUnit.DAYS).build())
            .setUserAgent("AndroidApp")

            .build()    


viewModelScope.launch {
            kNetworking.enqueue(kNetworkRequestGet, onSuccess = {
                _uiState.value = UiState.Success(it)
            }, onError = {
                _uiState.value = UiState.Error(it)
            })
        } 
```



### Making a POST Request
```kotlin
 val kNetworkRequestPost : KNetworkRequest = kNetworking.newPostRequestBuilder(url = "https://abcexample.in/api/post")
            .setTag("TestTag1")
            .setPriority(Priority.MEDIUM)

            .setHeaders("Header1","HeaderValue1")
            .setHeaders(mapOf(Pair("Header2","HeaderValue2")))
            .setHeaders(mutableMapOf(Pair("Header3","HeaderValue3")))
            .setHeaders(hashMapOf(Pair("Header4","HeaderValue4")))
            .setHeaders(TestHeader("Zahgr@1376n","1234"))

            .setQueryParameter("QueryParam1","QueryParamValue1")
            .setQueryParameter(mapOf(Pair("QueryParam2","QueryParamValue2")))
            .setQueryParameter(mutableMapOf(Pair("QueryParam3","QueryParamValue3")))
            .setQueryParameter(hashMapOf(Pair("QueryParam4","QueryParamValue4")))
            .setQueryParameter(TestQueryParam("QueryParamValue5"))

            .setPathParameter("PathParam1","PathParamValue1")
            .setPathParameter(mapOf(Pair("PathParam2","PathParamValue2")))
            .setPathParameter(mutableMapOf(Pair("PathParam3","PathParamValue3")))
            .setPathParameter(hashMapOf(Pair("PathParam4","PathParamValue4")))
            .setPathParameter(TestPathParam("PathParamValue5"))
                
            .setBodyParameter("TestBody","TestBodyValue1")
            .setBodyParameter(mapOf(Pair("TestBody2","TestBodyValue2")))
            .setBodyParameter(mutableMapOf(Pair("TestBody3","TestBodyValue3")))
            .setBodyParameter(hashMapOf(Pair("TestBody4","TestBodyValue4")))
            .setBodyParameter(TestBodyParam("TestBodyParam5"))

            .setCacheControl(CacheControl.Builder().noCache().maxAge(1,TimeUnit.DAYS).build())
            .setUserAgent("AndroidApp")

            .build()

viewModelScope.launch {
            kNetworking.enqueue(kNetworkRequestPost, onSuccess = {
                _uiState.value = UiState.Success(it)
            }, onError = {
                _uiState.value = UiState.Error(it)
            })
        } 
```
 
