package com.nedkuj.github.network.api

import com.nedkuj.github.model.response.ResponseObject
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubAPI {

    @GET("/search/repositories")
    fun getRepositories(
            @Query("page") page: Int,
            @Query("per_page") perPage: Int,
            @Query("q") query: String,
            @Query("sort") sort : String?
    ): Single<ResponseObject>

}