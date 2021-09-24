package com.nedkuj.github.network

interface Interceptor {
  fun getApplicationInterceptors(): List<okhttp3.Interceptor>
  fun loggingInterceptor(): okhttp3.Interceptor

  var token: String
}
