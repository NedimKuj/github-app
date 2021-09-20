package com.nedkuj.github.network

import okhttp3.OkHttpClient

interface HttpClient {
    fun getClient(): OkHttpClient
}
