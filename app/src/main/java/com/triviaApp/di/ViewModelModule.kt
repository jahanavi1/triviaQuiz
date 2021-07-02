package com.triviaApp.di


import com.triviaApp.BaseResponse
import com.triviaApp.utils.AppUtils.Companion.activity
import com.triviaApp.viewmodel.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * @author Leopold
 */
val viewModelModule = module(override = true) {
    viewModel { SplashViewModel(activity, Any::class.java) }
    viewModel { QuizViewModel(activity, Any::class.java) }

}
