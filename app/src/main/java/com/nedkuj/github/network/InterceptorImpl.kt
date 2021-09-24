package com.nedkuj.github.network

import android.util.Log
import com.nedkuj.github.BuildConfig
import com.orhanobut.logger.Logger
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import javax.inject.Inject

class InterceptorImpl @Inject constructor() :
    BaseInterceptor(),
    Interceptor {

    override var token: String = ""

    override fun loggingInterceptor(): okhttp3.Interceptor {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            if (BuildConfig.DEBUG) {
                Log.e("API", message)
            } else {
                Logger.json(message)
            }
        }
        loggingInterceptor.level = BODY
        return loggingInterceptor
    }

    override fun getApplicationInterceptors(): List<okhttp3.Interceptor> =
        listOf(requestInterceptor(), responseInterceptor(), loggingInterceptor())

}
