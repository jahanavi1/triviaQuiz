package com.triviaApp


import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.security.ProviderInstaller
import com.triviaApp.di.apiModule
import com.triviaApp.di.viewModelModule
import org.koin.core.context.startKoin
import javax.net.ssl.SSLContext

/**
 * @author Leopold
 */

class TriviaApp : Application() {

   companion object {
        var instance: TriviaApp? = null
    }


    override fun onCreate() {
        super.onCreate()

        try {
            // Google Play will install latest OpenSSL
            ProviderInstaller.installIfNeeded(applicationContext)
            val sslContext: SSLContext = SSLContext.getInstance("TLSv1.2")
            sslContext.init(null, null, null)
            sslContext.createSSLEngine()
        } catch (e: GooglePlayServicesRepairableException) {
            e.printStackTrace()
        }

        instance = this

        startKoin {
            // declare used Android context
//            androidContext(this@LayPay)
            // declare modules
            modules(apiModule, viewModelModule)
        }

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(p0: Activity, p1: Bundle?) {
//                mActivity = WeakReference(p0)
            }

            override fun onActivityStarted(p0: Activity) {

            }

            override fun onActivityResumed(p0: Activity) {

            }

            override fun onActivityPaused(p0: Activity) {

            }

            override fun onActivityStopped(p0: Activity) {

            }

            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {

            }

            override fun onActivityDestroyed(p0: Activity) {

            }

        })

    }

}