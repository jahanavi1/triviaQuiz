package com.triviaApp.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import android.util.Log
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport
import com.triviaApp.constant.AppConst
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class Prefs private constructor(private val context: Context) {
    private val SP_NAME = this.javaClass.getPackage()!!.name
    private val WALK_THROUGH = "prefs_walkThrough"
    private val REMEMBER_PREF = "prefs_rememberMe"
    private var userSharedPrefs: SharedPreferences? = null
    private var preferenceScreen: SharedPreferences? = null
    private lateinit var rememberPrefs: SharedPreferences


    fun clearUserCredentials(){
        rememberPrefs.edit().clear().apply()
    }

    init {
        userSharedPrefs = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        preferenceScreen = context.getSharedPreferences(WALK_THROUGH, Context.MODE_PRIVATE)
        rememberPrefs = context.getSharedPreferences(REMEMBER_PREF, Context.MODE_PRIVATE)

    }

    fun clearPrefs() {
        userSharedPrefs!!.edit().clear().apply()
    }

    companion object {
        private var instance: Prefs? = null

        fun getInstance(context: Context?): Prefs {
            if (instance == null)  // NOT thread safe!
                instance = Prefs(context!!)

            return instance!!
        }
    }


}
