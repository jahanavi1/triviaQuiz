package com.triviaApp

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/*{"successFlag":true,"responseMessage":"Status! -> User Successfully Registered!","errorResponseDTO":null,"response":null,"totalCount":0}*/

open class BaseResponse<T> {

    @SerializedName("response")
    @Expose
    var response: T? = null

    @SerializedName("http_status")
    @Expose
    var http_status: String? = null

    @SerializedName("status")
    @Expose
    var status: Int? = null

    @SerializedName("response_message")
    @Expose
    var message: String? = ""


    @SerializedName("error")
    @Expose
    var error: String? = null

    @SerializedName("total_count")
    @Expose
    var total_count: Int = 0


}
