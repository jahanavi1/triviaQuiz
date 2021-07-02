package com.triviaApp.viewmodel


import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.reflect.TypeToken
import com.triviaApp.core.BaseViewModel
import com.triviaApp.dataService.ApiRequest
import com.triviaApp.dataService.AppInterActor
import com.triviaApp.dataService.NetworkModule
import com.triviaApp.model.Quiz


class QuizViewModel<T>(context: AppCompatActivity, clazz: Class<T>) :
    BaseViewModel<T>(context, clazz) {

    private val QUIZ = 1
    private var faqResponse: Quiz = Quiz()


    fun getFAQ() {

        AppInterActor.instance!!.callGetServiceWithoutParameterApi(
            this.activity,
            ApiRequest.QUIZ,
            NetworkModule.primaryService,
            this,
            QUIZ
        )
    }

    override fun onResponse(response: String, responseCode: Int) {
        onFinish()
        if (responseCode == QUIZ) {
            Log.d("QUIZZZZZ","QUIZZZZZ"+response
            )
            Log.d("ANSSSSS","ANSSSSS"+ faqResponse.answer)

            faqResponse = parseArray(
                json = response,
                typeToken = object : TypeToken<MutableList<Quiz>>() {}.type
            )
            liveData.value = faqResponse
        }
    }

    override fun onError(response: String, responseCode: Int) {
        onFinish()
    }

}