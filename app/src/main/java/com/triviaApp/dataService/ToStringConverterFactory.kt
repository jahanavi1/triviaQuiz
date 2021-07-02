package com.triviaApp.dataService

import java.lang.reflect.Type

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit

class ToStringConverterFactory : Converter.Factory() {


    override fun responseBodyConverter(type: Type?, annotations: Array<Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
        return if (String::class.java == type) {
            Converter<ResponseBody, String> { value -> value.string() }
        } else null
    }


    override fun stringConverter(type: Type?, annotations: Array<Annotation>?, retrofit: Retrofit?): Converter<*, String>? {

        return if (String::class.java == type) {
            Converter<String, String> { value -> value }
        } else null
    }

    companion object {
        private val MEDIA_TYPE = "text/plain".toMediaType()
    }

}