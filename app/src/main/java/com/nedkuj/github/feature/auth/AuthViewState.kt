package com.nedkuj.github.feature.auth

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class AuthViewState

@Parcelize
data class AuthFullViewState(
    val loading: Boolean? = null,
    val error: Throwable? = null
) : Parcelable

class AuthSuccessViewState() : AuthViewState()
class AuthLoadingViewState(val loading: Boolean?) : AuthViewState()
class AuthErrorViewState(val error: Throwable?) : AuthViewState()
