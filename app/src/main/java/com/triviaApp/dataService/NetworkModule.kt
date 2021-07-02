package com.triviaApp.dataService


import android.util.Log
import com.triviaApp.BuildConfig
import com.triviaApp.TriviaApp
import com.triviaApp.utils.AppUtils
import com.triviaApp.constant.AppConst
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.apache.http.params.CoreConnectionPNames.CONNECTION_TIMEOUT
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.File
import java.util.concurrent.TimeUnit


object NetworkModule {


    private const val cacheSize = 5 * 1024 * 1024.toLong() // 5 MB
    private const val TAG = "ServiceGenerator"
    const val HEADER_CACHE_CONTROL = "Cache-Control"
    const val HEADER_PRAGMA = "Pragma"

    internal val loggingInterceptor: HttpLoggingInterceptor
        get() = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }


    val primaryService: RestService
        get() {
            val retrofit = Retrofit.Builder().baseUrl(AppConst.GOOGLE_BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(ToStringConverterFactory())
                    .client(getHttpClient())
                    .build()
            return retrofit.create(RestService::class.java)
        }


    internal fun getHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder().cache(cache())
                .addInterceptor(httpLoggingInterceptor()) // used if network off OR on
                .addNetworkInterceptor(networkInterceptor()) // only used when network is on
                .addInterceptor(offlineInterceptor()).
                writeTimeout(CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS).readTimeout(CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS).build()
    }

    private fun cache(): Cache {
        return Cache(File(TriviaApp.instance?.codeCacheDir, "someIdentifier"), cacheSize)
    }

    /**
     * This interceptor will be called both if the network is available and if the network is not available
     * @return
     */
    private fun offlineInterceptor(): Interceptor {
        return Interceptor { chain ->
            Log.d(TAG, "offline interceptor: called.")
            var request = chain.request()
            // prevent caching when network is on. For that we use the "networkInterceptor"
            if (!AppUtils.hasInternet(AppUtils.activity)) {
                val cacheControl = CacheControl.Builder()
                        .maxStale(7, TimeUnit.DAYS)
                        .build()
                request = request.newBuilder()
                        .removeHeader(HEADER_PRAGMA)
                        .removeHeader(HEADER_CACHE_CONTROL)
                        .cacheControl(cacheControl)
                        .build()
            }
            chain.proceed(request)
        }
    }

    /**
     * This interceptor will be called ONLY if the network is available
     * @return
     */
    private fun networkInterceptor(): Interceptor {
        return Interceptor { chain ->
            Log.d(TAG, "network interceptor: called.")
            val response = chain.proceed(chain.request())
            val cacheControl = CacheControl.Builder()
                    .maxAge(5, TimeUnit.SECONDS)
                    .build()
            response.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                    .build()
        }
    }

    private fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger{
            override fun log(message: String) {
                Log.d(TAG, "log: http log: $message")
            }
        })
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }


    val googleService: RestService
        get() {
            val retrofit = Retrofit.Builder().baseUrl(AppConst.GOOGLE_BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(ToStringConverterFactory())
                    .client(getHttpClient())
                    .build()
            return retrofit.create(RestService::class.java)
        }

//    Log.d(TAG, "log: http log: $message")

}
