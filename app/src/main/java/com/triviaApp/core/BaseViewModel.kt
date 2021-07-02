package com.triviaApp.core

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.triviaApp.constant.AppConst
import com.triviaApp.dataService.InterActorCallback
import com.triviaApp.utils.AppUtils
import com.triviaApp.utils.Prefs
import com.google.gson.GsonBuilder
import com.triviaApp.BaseResponse
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.lang.reflect.Type


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
open class BaseViewModel<T>(internal var activity: AppCompatActivity, clazz: Class<T>?) : ViewModel(),
    InterActorCallback {



    var baseHeaderMap = getHeader()

    val RESEND_EMAIL = 333
    var instance: T? = null
    val liveData: MutableLiveData<*> = MutableLiveData<Any>()

    private var commonResponse: BaseResponse<Any> = BaseResponse()
    var successResponse: BaseResponse<*> = BaseResponse<Any>()
    private val disposables: CompositeDisposable = CompositeDisposable()
    internal fun createContents(clazz: Class<T>): T? {
        try {
            this.instance = clazz.newInstance()
            return this.instance
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            return null
        } catch (e2: InstantiationException) {
            e2.printStackTrace()
            return null
        }

    }

    init {
//        (activity as BaseActivity).setAllPermissionsListener(this, true)

        if (clazz != null) {
            createContents(clazz)
        }
    }

    fun getHeader(): HashMap<String, String> {
        val headerMap = HashMap<String, String>()
        headerMap["Content-Type"] = "application/json"
//        headerMap["Authorization"] = AppUtils.getAccessToken(activity)
        headerMap["uuid"] = AppUtils.getUUID(activity)
//        headerMap["Authorization"] = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI5OTk5OTk5OTk5IiwiZXhwIjoxNjA0NzU1MzY0LCJpYXQiOjE2MDIxNjMzNjQsIlVzZXJJZCI6MywiUm9sZU5hbWUiOiJVU0VSIn0.vVgJ2qS4UB_CcuqhIHxAfVUTsHdXWw70PPYxH2QT17kDE4La0FqSCvhnR_tye4ypv2-Sw1OOKG5j-AYIkuGcAQ"
//        headerMap["uuid"] = "afa54725-8618-43f7-9f57-c85529b27e58"

        return headerMap
    }

    fun parseResponse(response: String): String? {
        onFinish()
        if(response.contains("failed to rename")){
            AppUtils.showErrorSnackBar("Something went wrong")
            return null
        }

        /*commonResponse = (this.activity as BaseActivity).gson!!.fromJson<Any>(
            response,
            BaseResponse::class.java
        ) as BaseResponse<Any>
        if (commonResponse.status == ResponseCode.SUCCESS.code) {
            return (this.activity as BaseActivity).gson!!.toJson(commonResponse.response)

        }else if (commonResponse.status == ResponseCode.FAILURE.code) {
            if(commonResponse.message.toString().contains("No Data Found")){
                return null
            } else  if(commonResponse.message.toString().contains("failed to rename")){
                return null
            }
            if(commonResponse.message!!.length>0){
                AppUtils.showErrorSnackBar(commonResponse.message.toString())
            }
            return null
        } else {
            return null
        }*/

        return response
    }


    inline fun <reified T> parseArray(json: String, typeToken: Type): T {
        val gson = GsonBuilder().create()
        return gson.fromJson<T>(json, typeToken)
    }


    fun changeMobileFormatForAPI(mobileNumber: String): String {
        /*val new  = mobileNumber.replace(" ", "")
        return new.replace("(", "").replace(")","")*/
        val regex = "\\D+".toRegex()
        return regex.replace(mobileNumber, "")
    }

    fun emptyViewVisibility(view: View, size: Int) {
        view.visibility = if (size == 0) View.VISIBLE else View.GONE
    }

    override fun onStart() {
        (this.activity as BaseActivity).showProgressDialog()
    }

    override fun onFinish() {
        (this.activity as BaseActivity).showProgressDialog(false)
    }

    override fun onResponse(response: String, responseCode: Int) {
        /*if (responseCode == RESEND_EMAIL) {
            var successResponse =
                (activity as BaseActivity).gson?.fromJson(response, SuccessResponse::class.java)
            if (successResponse?.status == ResponseCode.SUCCESS.code) {
                AppUtils.showSnackBar(successResponse?.message.toString())
            } else
                AppUtils.showErrorSnackBar(successResponse?.message.toString())
        }*/

    }

    override fun onError(response: String, responseCode: Int) {
        /*var successResponse =
            (activity as BaseActivity).gson?.fromJson(response, SuccessResponse::class.java)
        AppUtils.showErrorSnackBar("")
        val stringBuilder = StringBuilder()
        stringBuilder.append("onError: ")
        stringBuilder.append("")
        Log.i("TAG", stringBuilder.toString())*/
        onFinish()
        checkContainError(response)
    }


    fun addToDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

    var view: View? = null
    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }

    fun onUploadClick(view: View) {
        this.view = view
//        (activity as BaseActivity).requestForCameraStorage(true)
    }

    internal fun checkContainError(response: String) : Boolean {
        try {
            val baseResponse = (activity as BaseActivity).gson?.fromJson(response, BaseResponse::class.java)!!
            return  checkContainError(baseResponse)
        } catch (e:Exception){
            e.printStackTrace()
            AppUtils.showErrorSnackBar(response)
            return true
        }
    }

    internal fun checkContainError(baseResponse: BaseResponse<*>) : Boolean {
        var hasError =  false
        try {

            if(baseResponse.status != null && (baseResponse.status == 404 || baseResponse.status == 503)){
                AppUtils.showErrorSnackBar(baseResponse.error.toString())
                hasError = true
            } else if(baseResponse.error.toString().equals("UNAUTHORIZED", true)
                || baseResponse.http_status.toString().equals("UNAUTHORIZED", true)){
                Prefs.getInstance(activity).clearPrefs()
//                val intent = Intent(activity, LoginActivity::class.java)
//                intent.putExtra(AppConst.FROM, "Logout")
//                activity.startActivity(intent)
                activity.finish()
            }

            return  hasError
        } catch (e:Exception){
            AppUtils.showSnackBar("Something went wrong")
            return hasError
        }

    }
}

