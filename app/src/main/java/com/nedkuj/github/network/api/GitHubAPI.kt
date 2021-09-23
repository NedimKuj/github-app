package com.nedkuj.github.network.api

import com.nedkuj.github.model.Owner
import com.nedkuj.github.model.response.RepoResponseObject
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubAPI {

    @GET("/search/repositories")
    fun getRepositories(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("q") query: String,
        @Query("sort") sort: String?
    ): Single<RepoResponseObject>

    @GET("/users/{username}")
    fun getUser(@Path("username") username: String): Single<Owner>
}