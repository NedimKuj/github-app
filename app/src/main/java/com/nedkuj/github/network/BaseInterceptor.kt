package com.nedkuj.github.network

abstract class BaseInterceptor {

    abstract var token: String

    fun requestInterceptor(): okhttp3.Interceptor {
        return okhttp3.Interceptor { chain ->
            val builder = chain.request()
                .newBuilder()
            builder.addHeader("Accept", "application/json")
            if (token.isNotEmpty()) {
                builder.addHeader("Authorization", "token $token")
            }
            chain.proceed(builder.build())
        }
    }

    fun responseInterceptor(): okhttp3.Interceptor {
        return okhttp3.Interceptor { chain ->
            val response = chain.proceed(chain.request())
            response
        }
    }
}
