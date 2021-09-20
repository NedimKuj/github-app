package com.nedkuj.github.network

import com.squareup.moshi.Moshi

interface Serializer {
    fun getMoshi(): Moshi
}
