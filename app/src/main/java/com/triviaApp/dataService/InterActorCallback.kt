package com.triviaApp.dataService


interface InterActorCallback {

    fun onStart()

    fun onResponse(response: String, responseCode: Int)

    fun onFinish()

    fun onError(message: String, responseCode: Int)


}
