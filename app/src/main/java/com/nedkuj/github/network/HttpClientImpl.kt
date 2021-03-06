package com.nedkuj.github.network

import android.content.Context
import com.nedkuj.github.network.session.Session
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HttpClientImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val interceptor: Interceptor,
    val session: Session
) : HttpClient {
    override fun getClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)

        session.hasSession().blockingFirst()?.let { hasSession ->
            if (hasSession) {
                interceptor.token = session.get().blockingFirst().token
            }
        }

        interceptor.getApplicationInterceptors()
            .forEach {
                okHttpClientBuilder.addInterceptor(it)
                okHttpClientBuilder.addInterceptor(ChuckInterceptor(context))
            }

        return okHttpClientBuilder.build()
    }
}
