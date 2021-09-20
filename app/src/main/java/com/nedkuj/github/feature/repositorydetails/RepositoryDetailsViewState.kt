package com.nedkuj.github.feature.repositorydetails

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class RepositoryDetailsViewState

@Parcelize
data class RepositoryDetailsFullViewState(
    val loading: Boolean? = null,
    val error: Throwable? = null
) : Parcelable

class RepositoryDetailsSuccessViewState() : RepositoryDetailsViewState()
class RepositoryDetailsLoadingViewState(val loading: Boolean?): RepositoryDetailsViewState()
class RepositoryDetailsErrorViewState(val error: Throwable?): RepositoryDetailsViewState()