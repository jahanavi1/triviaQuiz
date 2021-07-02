package com.triviaApp.dataService

import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import java.util.*


interface RestService {
    @GET
    fun apiGET(@HeaderMap header: Map<String, String>?, @Url url: String?): Observable<String?>?


    @POST
    fun apiPost(@HeaderMap header: Map<String, String>?, @Url url: String?): Observable<String?>?

    @POST
    fun apiPost(@HeaderMap header: Map<String?, String?>?, @Url url: String?, @Body request: RequestBody?): Observable<String?>?

    @FormUrlEncoded
    @POST
    fun apiPost(@HeaderMap header: Map<String, String>?, @Url url: String, @FieldMap request:@JvmSuppressWildcards Map<String, Any>?): Observable<String?>?

    @FormUrlEncoded
    @POST
    fun apiPost(@Url url: String?, @FieldMap request: Map<String?, Any?>?): Observable<String?>?


    @POST
    fun apiPost(@Header("Content-Type") content_type: String?, @Url url: String?, @Body request: RequestBody?): Observable<String?>?


    @POST
    fun apiPost(@Url url: String?, @QueryMap requestMap: HashMap<String, Any>?): Observable<String?>?

    @POST
    fun apiPost(@Url url: String?,@HeaderMap header: Map<String, String>?, @Body request: RequestBody?): Observable<String?>?
    @POST
    fun apiPost(@Url url: String?, @Body request: RequestBody?): Observable<String?>?

    @GET
    fun apiGET(@Url urlt: String?): Observable<String?>?

    @Multipart
    @POST
    fun apiMultiPartPostFile(@Url url: String?, @PartMap bodyPart: Map<String, RequestBody>?, @Part vararg body: MultipartBody.Part?): Observable<String?>?


    @Multipart
    @POST
    fun apiPartMapWithoutFilePost(@Url url: String?, @PartMap params: Map<String, RequestBody>?): Observable<String?>?

    @Multipart
    @POST
    fun apiMultiPartPostWithHeaderWithoutFile(@HeaderMap header: Map<String?, String?>?, @Url url: String?, @PartMap bodyPart: Map<String, RequestBody>): Observable<String?>?


    @Multipart
    @POST
    fun apiPartMapFileWithHeaderPost(@HeaderMap header: Map<String?, String?>?, @Url url: String?, @PartMap params: Map<String, RequestBody>?, @Part vararg body: MultipartBody.Part?): Observable<String?>?


    @Multipart
    @POST
    fun apiPartMapWithHeader(@HeaderMap header: Map<String?, String?>?, @Url url: String?, @Part vararg body: MultipartBody.Part?): Observable<String?>?

    @Multipart
    @POST
    fun apiPartMapWithOUTHeader(@Url url: String?, @Part vararg body: MultipartBody.Part?): Observable<String?>?



}
