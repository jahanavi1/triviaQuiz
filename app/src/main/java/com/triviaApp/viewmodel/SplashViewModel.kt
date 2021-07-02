package com.triviaApp.viewmodel


import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.reflect.TypeToken
import com.triviaApp.BaseResponse
import com.triviaApp.core.BaseViewModel
import com.triviaApp.dataService.ApiRequest
import com.triviaApp.dataService.AppInterActor
import com.triviaApp.dataService.NetworkModule
import com.triviaApp.utils.Prefs

class SplashViewModel<T>(context: AppCompatActivity, clazz: Class<T>) : BaseViewModel<T>(context, clazz) {

    var scheduleTime: Long = 3000
    private var GET_CONFIG_LIST = 1
    private var GET_PENDING_REVIEW = 2
    var mhandlerResponse: HandlerResponse? = null
//    private var configResponse: BaseResponse<*> = BaseResponse<ResponseList<Config>>()

    interface HandlerResponse {
        fun onSuccess()
    }

    init {
//        callConfigList()

    }

    fun setHandler(mhandler: HandlerResponse) {

        this.mhandlerResponse = mhandler
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            onFinish()
            mhandlerResponse?.onSuccess()
        }, scheduleTime)

    }

//    fun callConfigList() {
//        if (Prefs.getInstance(this.activity).accessToken == "") {
//            AppInterActor.instance?.callGetServiceWithoutParameterApi(
//                activity,
//                ApiRequest.GET_CONFIG,
//                NetworkModule.primaryService,
//                this,
//                GET_CONFIG_LIST
//            )
//        } else {
//
//            AppInterActor.instance?.callGETServiceWithHeaderOnly(
//                activity,
//                baseHeaderMap,
//                ApiRequest.GET_CONFIG,
//                NetworkModule.primaryService,
//                this,
//                GET_CONFIG_LIST
//            )
//        }
//    }

    override fun onResponse(response: String, responseCode: Int) {

//        if (responseCode == GET_CONFIG_LIST) {
//            val data = parseResponse(response)
//            if (data != null) {
//                configResponse = parseArray(
//                    json = response,
//                    typeToken = object : TypeToken<BaseResponse<ResponseList<Config>>>() {}.type
//                )
//                liveData.value = configResponse
//
////                Prefs.getInstance(activity as Context?).setConfig((configResponse.response as BaseResponse<ResponseList<Config>>).response!!.responseList as ArrayList<Config>)
//            }
//        }
    }
}