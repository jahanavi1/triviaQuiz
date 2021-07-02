package com.triviaApp.dataService

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.view.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.triviaApp.BaseResponse
import com.triviaApp.R
import com.triviaApp.utils.AppUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.util.*

class AppInterActor {
    var dialog: Dialog? = null

    @SuppressLint("CheckResult")
    fun callServiceApi(
        context:Context,
        headerMap: Map<String, String>,
        request: Map<String, Any>,
        subURL: String?,
        restService: RestService,
        callback: InterActorCallback,
        requestCode: Int
    ) { //        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), request);
        if (AppUtils.hasInternet(context)) {
            callback.onStart()
            if (subURL != null) {
                restService.apiPost(headerMap, subURL, request)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(
                        { o: String? ->
                            callback.onResponse(
                                o.toString(),
                                requestCode
                            )
                        },
                        { throwable: Throwable ->
                            callback.onError(
                                (throwable as HttpException).response()?.errorBody()?.string().toString(),
                                requestCode
                            )
                        }
                    )
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("CheckResult")
    fun callGETServiceWithHeaderOnly(
        activity: AppCompatActivity,
        headerMap: Map<String, String>,
        subURL: String,
        restService: RestService,
        callback: InterActorCallback,
        responseCode: Int, showProgress:Boolean = true
    ) {
        if (AppUtils.hasInternet(activity)) {
            if(showProgress)
                callback.onStart()
            restService.apiGET(headerMap, subURL)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                    { o: String? ->
                        callback.onResponse(
                            o.toString(),
                            responseCode
                        )
                    }
                ) { throwable: Throwable ->
                    if((throwable as HttpException).code() == 404 || (throwable as HttpException).code() == 503 ){
                        val baseResponse = BaseResponse<Any>()
                        baseResponse.status = throwable.code()
                        baseResponse.error = "Something went wrong"
                        val responseString = Gson().toJson(baseResponse)
                        callback.onError(
                            responseString,
                            responseCode
                        )
                    } else {
                        callback.onError(
                            (throwable as HttpException).response()?.errorBody()?.string().toString(),
                            responseCode
                        )
                    }
                }
        } else noInternetForWithoutParameterDialog(
            activity,
            headerMap,
            subURL,
            restService,
            callback,
            responseCode
        )
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("CheckResult")
    fun callGetServiceWithoutParameterApi(
        activity: AppCompatActivity,
        subURL: String,
        restService: RestService,
        callback: InterActorCallback,
        responseCode: Int
    ) {
        if (AppUtils.hasInternet(activity)) {
            callback.onStart()
            restService.apiGET(subURL)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                    { o: String? ->
                        callback.onResponse(
                            o.toString(),
                            responseCode
                        )
                    }
                ) { throwable: Throwable ->
                    if((throwable as HttpException).code() == 404 || (throwable as HttpException).code() == 503 ){
                        val baseResponse = BaseResponse<Any>()
                        baseResponse.status = throwable.code()
                        baseResponse.error = "Something went wrong"
                        val responseString = Gson().toJson(baseResponse)
                        callback.onError(
                            responseString,
                            responseCode
                        )
                    } else {
                        callback.onError(
                            (throwable as HttpException).response()?.errorBody()?.string().toString(),
                            responseCode
                        )
                    }
                }
        } else noInternetForGETWithoutParameterDialog(activity,subURL, restService, callback, responseCode)
    }

    @SuppressLint("CheckResult")
    fun callServiceApi(
        request: String?, subURL: String?,
        restService: RestService, callback: InterActorCallback, responseCode: Int
    ) {
        callback.onStart()
//        val body = RequestBody.create(MediaType.parse("application/json"), request)
        val body = request.toString().toRequestBody("application/json".toMediaTypeOrNull())
        restService.apiPost(subURL!!, body)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                { o: String? ->
                    callback.onResponse(
                        o
                            .toString(), responseCode
                    )
                }
            ) { throwable: Throwable ->
                callback.onError(
                    (throwable as HttpException).response()?.errorBody()?.string().toString(),
                    responseCode
                )
            }
    }

    @SuppressLint("CheckResult")
    fun callServiceApi(
        request: String?, header: Map<String, String>?, subURL: String?,
        restService: RestService, callback: InterActorCallback, responseCode: Int
    ) {
        callback.onStart()
//        val body = RequestBody.create(MediaType.parse("application/json"), request)
        val body = request.toString().toRequestBody("application/json".toMediaTypeOrNull())
        /*restService.apiPost(subURL!!,header, body)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                { onNext: String? ->
                    callback.onResponse(
                        onNext.toString(), responseCode
                    )
                }
            ) { throwable: Throwable ->
                callback.onError(
                    throwable.message!!,
                    responseCode
                )
            }*/

        restService.apiPost(subURL!!, header, body)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                { onNext: String? ->
                    try {

                        callback.onResponse(
                            onNext.toString(), responseCode
                        )
                    } catch (e:Exception){
                        e.printStackTrace()
                    }
                },
                { throwable: Throwable ->
                    try {
                        if((throwable as HttpException).code() == 404 || (throwable as HttpException).code() == 503 ){
                            val baseResponse = BaseResponse<Any>()
                            baseResponse.status = throwable.code()
                            baseResponse.error = "Something went wrong"
                            val responseString = Gson().toJson(baseResponse)
                            callback.onError(
                                responseString,
                                responseCode
                            )
                        } else {
                            callback.onError(
                                (throwable as HttpException).response()?.errorBody()?.string().toString(),
                                responseCode
                            )
                        }
                    } catch (e:Exception){
                        e.printStackTrace()
                        callback.onError(
                            "",
                            responseCode
                        )
                    }
                })
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("CheckResult")
    fun callServicePartMapApi(
        activity: AppCompatActivity,
        requestMap: Map<String, RequestBody>,
        subURL: String,
        restService: RestService,
        callback: InterActorCallback,
        multipartBody: Array<MultipartBody.Part?>,
        responseCode: Int
    ) {
        if (AppUtils.hasInternet(activity)) {
            callback.onStart()
            //            RequestBody body = RequestBody.create(MediaType.parse("application/json"), request);
            restService.apiMultiPartPostFile(subURL, requestMap, *multipartBody)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                    { o: String? ->
                        callback.onResponse(
                            o.toString(),
                            responseCode
                        )
                    }
                ) { throwable: Throwable ->
                    callback.onError(
                        (throwable as HttpException).response()?.errorBody()?.string().toString(),
                        responseCode
                    )
                }
        } else noInternetForMultipartWithFileDialog(
            activity,
            requestMap,
            subURL,
            restService,
            callback,
            multipartBody,
            responseCode
        )
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("CheckResult")
    fun callServicePartMapWithouFileApi(
        activity: AppCompatActivity,
        requestMap: Map<String, RequestBody>,
        subURL: String,
        restService: RestService,
        callback: InterActorCallback,
        responseCode: Int
    ) {
        if (AppUtils.hasInternet(activity)) {
            callback.onStart()
            //            RequestBody body = RequestBody.create(MediaType.parse("application/json"), request);
            restService.apiPartMapWithoutFilePost(subURL, requestMap)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                    { o: String? ->
                        callback.onResponse(
                            o.toString(),
                            responseCode
                        )
                    }
                ) { throwable: Throwable ->
                    callback.onError(
                        (throwable as HttpException).response()?.errorBody()?.string().toString(),
                        responseCode
                    )
                }
        } else noInternetForMultipartWithoutFileDialog(
            activity,
            requestMap,
            subURL,
            restService,
            callback,
            responseCode
        )
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("CheckResult")
    fun callServicePartMapWithHeaderApi(
        activity: AppCompatActivity,
        headerMap: Map<String?, String?>?,
        requestMap: Map<String, RequestBody>,
        subURL: String,
        restService: RestService,
        callback: InterActorCallback,
        multipartBody: Array<MultipartBody.Part?>,
        responseCode: Int
    ) {
        if (AppUtils.hasInternet(activity)) {
            callback.onStart()
            //            RequestBody body = RequestBody.create(MediaType.parse("application/json"), request);
            restService.apiPartMapFileWithHeaderPost(
                headerMap,
                subURL,
                requestMap,
                multipartBody[0]
            )
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                    { o: String? ->
                        callback.onResponse(
                            o.toString(),
                            responseCode
                        )
                    }
                ) { throwable: Throwable ->
                    callback.onError(
                        (throwable as HttpException).response()?.errorBody()?.string().toString(),
                        responseCode
                    )
                }
        } else noInternetForMultipartWithFileDialog(
            activity,
            requestMap,
            subURL,
            restService,
            callback,
            multipartBody,
            responseCode
        )
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("CheckResult")
    fun callServicePArtMapWithHeader(
        activity: AppCompatActivity,
        headerMap: Map<String?, String?>?,
        subURL: String,
        restService: RestService,
        callback: InterActorCallback,
        multipartBody: Array<MultipartBody.Part?>,
        responseCode: Int
    ) {
        if (AppUtils.hasInternet(activity)) {
            callback.onStart()
            //            RequestBody body = RequestBody.create(MediaType.parse("application/json"), request);
            restService.apiPartMapWithHeader(headerMap, subURL, multipartBody[0])
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                    { o: String? ->
                        callback.onResponse(
                            o.toString(),
                            responseCode
                        )
                    }
                ) { throwable: Throwable ->
                    callback.onFinish()
                    callback.onError(
                        (throwable as HttpException).response()?.errorBody()?.string().toString(),
                        responseCode
                    )
                }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("CheckResult")
    fun callServicePArtMapWithOutHeader(
        activity: AppCompatActivity,
        subURL: String,
        restService: RestService,
        callback: InterActorCallback,
        multipartBody: Array<MultipartBody.Part?>,
        responseCode: Int
    ) {
        if (AppUtils.hasInternet(activity)) {
            callback.onStart()
            //            RequestBody body = RequestBody.create(MediaType.parse("application/json"), request);
            restService.apiPartMapWithOUTHeader(subURL, multipartBody[0])
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                    { o: String? ->
                        callback.onResponse(
                            o.toString(),
                            responseCode
                        )
                    }
                ) { throwable: Throwable ->
                    callback.onFinish()
                    callback.onError(
                        (throwable as HttpException).response()?.errorBody()?.string().toString(),
                        responseCode
                    )
                }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("CheckResult")
    fun callServicePartMapHeaderWithoutFileApi(
        activity: AppCompatActivity,
        headerMap: Map<String?, String?>?,
        requestMap: Map<String, RequestBody>,
        subURL: String,
        restService: RestService,
        callback: InterActorCallback,
        responseCode: Int
    ) {
        if (AppUtils.hasInternet(activity)) {
            callback.onStart()
            //            RequestBody body = RequestBody.create(MediaType.parse("application/json"), request);
            restService.apiMultiPartPostWithHeaderWithoutFile(headerMap, subURL, requestMap)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                    { o: String? ->
                        callback.onResponse(
                            o.toString(),
                            responseCode
                        )
                    }
                ) { throwable: Throwable ->
                    callback.onError(
                        (throwable as HttpException).response()?.errorBody()?.string().toString(),
                        responseCode
                    )
                }
        } else noInternetForMultipartWithoutFileDialog(
            activity,
            requestMap,
            subURL,
            restService,
            callback,
            responseCode
        )
    }

    fun callPostServiceApiWithoutHeader(
        activity: AppCompatActivity,
        subURL: String?,
        restService: RestService,
        map: HashMap<String?, Any?>?,
        callback: InterActorCallback,
        responseCode: Int
    ) {
        if (AppUtils.hasInternet(activity)) {
            callback.onStart()
            //            RequestBody body = RequestBody.create(MediaType.parse("application/json"), request);
            restService.apiPost(subURL, map)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                    Consumer { o: String? ->
                        callback.onResponse(
                            o.toString(),
                            responseCode
                        )
                    },
                    Consumer { throwable: Throwable ->
                        callback.onError(
                            (throwable as HttpException).response()?.errorBody()?.string().toString(),
                            responseCode
                        )
                    }
                )
        } else { //            noInternetForMultipartWithoutFileDialog(requestMap, subURL, restService, callback, responseCode);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private fun noInternetForMultipartWithoutFileDialog(
        activity: AppCompatActivity,
        map: Map<String, RequestBody>,
        subURL: String,
        restService: RestService,
        callback: InterActorCallback,
        responseCode: Int
    ) {
        createDialog(activity)
        dialog!!.setOnKeyListener { arg0: DialogInterface?, keyCode: Int, event: KeyEvent? ->
            // TODO Auto-generated method stub
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialog!!.dismiss()
                if (AppUtils.hasInternet(activity)) {
                    callServicePartMapWithouFileApi(
                        activity,
                        map,
                        subURL,
                        restService,
                        callback,
                        responseCode
                    )
                } else {
                    activity.finishAffinity()
                }
            }
            true
        }
        dialog!!.findViewById<View>(R.id.FUNCTION)
            .setOnClickListener { v: View? ->
                if (AppUtils.hasInternet(activity)) {
                    callServicePartMapWithouFileApi(
                        activity,
                        map,
                        subURL,
                        restService,
                        callback,
                        responseCode
                    )
                    dialog!!.dismiss()
                } else AppUtils.showToast(
                    activity,
                    activity.getString(R.string.error_no_connection)
                )
            }
        dialog!!.show()
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private fun noInternetForMultipartWithFileDialog(
        activity: AppCompatActivity,
        map: Map<String, RequestBody>,
        subURL: String,
        restService: RestService,
        callback: InterActorCallback,
        multipartBody: Array<MultipartBody.Part?>,
        responseCode: Int
    ) {
        createDialog(activity)
        dialog!!.setOnKeyListener { arg0: DialogInterface?, keyCode: Int, event: KeyEvent? ->
            // TODO Auto-generated method stub
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialog!!.dismiss()
                if (AppUtils.hasInternet(activity)) {
                    callServicePartMapApi(
                        activity,
                        map,
                        subURL,
                        restService,
                        callback,
                        multipartBody,
                        responseCode
                    )
                } else {
                    activity.finishAffinity()
                }
            }
            true
        }
        dialog!!.findViewById<View>(R.id.FUNCTION)
            .setOnClickListener {
                if (AppUtils.hasInternet(activity)) {
                    callServicePartMapApi(
                        activity,
                        map,
                        subURL,
                        restService,
                        callback,
                        multipartBody,
                        responseCode
                    )
                    dialog!!.dismiss()
                } else AppUtils.showToast(
                    activity,
                    activity.getString(R.string.error_no_connection)
                )
            }
        dialog!!.show()
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private fun noInternetForWithoutParameterDialog(
        activity: AppCompatActivity,
        headerMap: Map<String, String>,
        subURL: String,
        restService: RestService,
        callback: InterActorCallback,
        responseCode: Int
    ) {
        createDialog(activity)
        dialog!!.setOnKeyListener { _: DialogInterface?, keyCode: Int, _: KeyEvent? ->
            // TODO Auto-generated method stub
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialog!!.dismiss()
                if (AppUtils.hasInternet(activity)) {
                    callGETServiceWithHeaderOnly(
                        activity,
                        headerMap,
                        subURL,
                        restService,
                        callback,
                        responseCode
                    )
                } else {
                    activity.finishAffinity()
                }
            }
            true
        }
        dialog!!.findViewById<View>(R.id.btn_try_again)
            .setOnClickListener {
                if (AppUtils.hasInternet(activity)) {
                    callGETServiceWithHeaderOnly(
                        activity,
                        headerMap,
                        subURL,
                        restService,
                        callback,
                        responseCode
                    )
                    dialog!!.dismiss()
                } else AppUtils.showToast(
                    activity,
                    activity.getString(R.string.error_no_connection)
                )
            }
        dialog!!.show()
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private fun noInternetForGETWithoutParameterDialog(
        activity: AppCompatActivity,
        subURL: String,
        restService: RestService,
        callback: InterActorCallback,
        responseCode: Int
    ) {
        createDialog(activity)
        dialog!!.setOnKeyListener { _: DialogInterface?, keyCode: Int, _: KeyEvent? ->
            // TODO Auto-generated method stub
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialog!!.dismiss()
                if (AppUtils.hasInternet(activity)) {
                    callGetServiceWithoutParameterApi(activity, subURL, restService, callback, responseCode)
                } else {
                    activity.finishAffinity()
                }
            }
            true
        }
        dialog!!.findViewById<View>(R.id.FUNCTION)
            .setOnClickListener {
                if (AppUtils.hasInternet(activity)) {
                    callGetServiceWithoutParameterApi(activity, subURL, restService, callback, responseCode)
                    dialog!!.dismiss()
                } else AppUtils.showToast(
                   activity,
                    activity.getString(R.string.error_no_connection)
                )
            }
        dialog!!.show()
    }

    private fun createDialog(activity: AppCompatActivity) {
        dialog = Dialog(
            activity,
            android.R.style.Theme_DeviceDefault_Wallpaper
        )
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (activity != null) {
            val inflater =
                LayoutInflater.from(activity)
            val view: View = inflater.inflate(R.layout.dialog_no_internet, null)
            dialog!!.setContentView(view)
            val window = dialog!!.window
            val wlp = window!!.attributes
            wlp.gravity = Gravity.CENTER
            //            wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
            window.attributes = wlp
            dialog!!.window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
            dialog!!.setCancelable(false)
            dialog!!.show()
        }
    }

    companion object {
        private const val TAG = "AppInteractor"
        var appInterActor: AppInterActor? = null
        val instance: AppInterActor?
            get() {
                if (appInterActor == null) {
                    appInterActor = AppInterActor()
                }
                return appInterActor
            }
    }


}