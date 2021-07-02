package com.triviaApp.core

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.triviaApp.R

/**
 * @author Leopold
 */
abstract class BindingActivity<T : ViewDataBinding>() : BaseActivity() {
    @LayoutRes
    abstract fun getLayoutResId(): Int


    public var FULL_SCREEN_AD = 222

    public lateinit var mBinding: T


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, getLayoutResId())

    }

    fun changeStatusBarToLight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;//  set status text dark
            window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        }
    }
    fun changeStatusBarToColorPrimary() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;//  set status text dark
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
        }
    }

    fun changeStatusBarToLightFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;//  set status text dark
            window.statusBarColor = ContextCompat.getColor(this, R.color.white);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }
    }


//    fun reDirectToLogin(){
//        Prefs.getInstance(this).clearPrefs()
//
//    }




}