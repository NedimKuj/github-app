package com.nedkuj.github.feature.profile

import android.os.Parcelable
import com.nedkuj.github.model.AuthenticatedUser
import kotlinx.parcelize.Parcelize

sealed class ProfileViewState

@Parcelize
data class ProfileFullViewState(
    val loading: Boolean? = null,
    val error: Throwable? = null,
    val user: AuthenticatedUser? = null
) : Parcelable

class ProfileSuccessViewState(val user: AuthenticatedUser?) : ProfileViewState()
class ProfileLoadingViewState(val loading: Boolean?) : ProfileViewState()
class ProfileErrorViewState(val error: Throwable?) : ProfileViewState()
