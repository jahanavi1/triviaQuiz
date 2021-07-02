package com.triviaApp.activities

import android.content.Intent
import android.os.*
import com.triviaApp.R
import com.triviaApp.core.BindingActivity
import com.triviaApp.databinding.ActivitySplashBinding


class SplashActivity() : BindingActivity<ActivitySplashBinding>() {
    var handler: Handler? = null
    override fun getLayoutResId() = R.layout.activity_splash

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handler = Handler(Looper.getMainLooper())
        handler!!.postDelayed({
            val intent = Intent(this@SplashActivity, PlayGameActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}