package com.nedkuj.github.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Owner(
        val login: String,
        val avatar_url: String,
        val url: String,
        val type: String,
        val name: String?,
        val blog: String?,
        val location: String?,
        val email: String?,
        val bio: String?
) : Parcelable
