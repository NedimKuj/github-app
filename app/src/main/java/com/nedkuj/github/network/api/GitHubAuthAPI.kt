package com.nedkuj.github.network.api

import com.nedkuj.github.model.response.LoginResponse
import io.reactivex.Single
import retrofit2.http.POST
import retrofit2.http.Query

interface GitHubAuthAPI {

    @POST("login/oauth/access_token")
    fun getAccessToken(
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("code") code: String,
    ): Single<LoginResponse>
}