package com.nedkuj.github.network.repository

import com.nedkuj.github.BuildConfig
import com.nedkuj.github.di.ApiUrl
import com.nedkuj.github.network.HttpClient
import com.nedkuj.github.network.RxCallAdapterFactory
import com.nedkuj.github.network.Serializer
import com.nedkuj.github.network.api.GitHubAPI
import com.nedkuj.github.network.api.GitHubAuthAPI
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

class RetrofitClientImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val serializer: Serializer,
    @ApiUrl private val apiUrl: String
) : RetrofitClient {

    override fun getGitHubAPI(): GitHubAPI {
        return Retrofit.Builder()
            .baseUrl(apiUrl)
            .addConverterFactory(MoshiConverterFactory.create(serializer.getMoshi()))
            .addCallAdapterFactory(RxCallAdapterFactory.create())
            .client(httpClient.getClient())
            .build()
            .create(GitHubAPI::class.java)
    }

    override fun getGitHubAuthAPI(): GitHubAuthAPI {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.WEB)
            .addConverterFactory(MoshiConverterFactory.create(serializer.getMoshi()))
            .addCallAdapterFactory(RxCallAdapterFactory.create())
            .client(httpClient.getClient())
            .build()
            .create(GitHubAuthAPI::class.java)
    }
}
