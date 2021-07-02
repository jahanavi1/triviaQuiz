package com.triviaApp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Awesome Pojo Generator
 */
class Quiz {
    @SerializedName("question")
    @Expose
    var question: String? = null

    @SerializedName("answer")
    @Expose
    var answer: String? = null

    @SerializedName("key_name")
    @Expose
    var key_name: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("value")
    @Expose
    var value: String? = null

}