package com.nedkuj.github.network

abstract class BaseInterceptor {

    fun requestInterceptor(): okhttp3.Interceptor {
        return okhttp3.Interceptor { chain ->
            val builder = chain.request()
                .newBuilder()
            //            builder.addHeader("Authorization", token)
            //            builder.addHeader("Accept-Language", language)
            //            builder.addHeader("app-platform", "Android")
            //            builder.addHeader("app-version", BuildConfig.VERSION_NAME)
            //Code for intercepting and adjusting request
            chain.proceed(builder.build())
        }
    }

    fun responseInterceptor(): okhttp3.Interceptor {
        return okhttp3.Interceptor { chain ->
            val response = chain.proceed(chain.request())
            ///Code for intercepting and adjusting response
            response
        }
    }
}
