package com.nedkuj.github.di.application

import android.content.Context
import android.content.SharedPreferences
import com.nedkuj.github.BuildConfig
import com.nedkuj.github.di.ApiUrl
import com.nedkuj.github.network.*
import com.nedkuj.github.network.repository.RetrofitClient
import com.nedkuj.github.network.repository.RetrofitClientImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
open class ApplicationModule {

    @Singleton
    @Provides
    fun httpClient(httpClient: HttpClientImpl): HttpClient = httpClient

    @Singleton
    @Provides
    fun interceptor(interceptor: InterceptorImpl): Interceptor = interceptor

    @Singleton
    @Provides
    fun retrofit(retrofit: RetrofitClientImpl): RetrofitClient = retrofit

    @Singleton
    @Provides
    fun serializer(serializer: SerializerImpl): Serializer = serializer
    

    @Singleton
    @Provides
    @ApiUrl
    fun apiUrl(): String = BuildConfig.API

    @Singleton
    @Provides
    fun sharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(context.packageName, 0)
    
}
