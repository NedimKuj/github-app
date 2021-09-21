package com.nedkuj.github.model

data class SearchReposPayload(
        val page: Int,
        val perPage: Int,
        val query: String
)
