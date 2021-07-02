package com.triviaApp.di
import com.triviaApp.constant.AppConst
import com.triviaApp.dataService.NetworkModule.getHttpClient
import com.triviaApp.dataService.RestService
import com.triviaApp.dataService.ToStringConverterFactory
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
val apiModule = module (override = true){

        fun provideUserApi(): RestService {
        val retrofit = Retrofit.Builder().baseUrl(AppConst.GOOGLE_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ToStringConverterFactory())
                .client(getHttpClient())
                .build()
        return retrofit.create(RestService::class.java)
    }
    single(createdAtStart = false) { provideUserApi()  }


}


