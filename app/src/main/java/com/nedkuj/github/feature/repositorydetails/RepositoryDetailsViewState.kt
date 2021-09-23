package com.nedkuj.github.feature.repositorydetails

import android.os.Parcelable
import com.nedkuj.github.model.Repository
import kotlinx.parcelize.Parcelize

sealed class RepositoryDetailsViewState

@Parcelize
data class RepositoryDetailsFullViewState(
    val loading: Boolean? = null,
    val error: Throwable? = null,
    val repository: Repository? = null,
    val url: String? = null
) : Parcelable

class RepositoryDetailsSuccessViewState(val repository: Repository?) : RepositoryDetailsViewState()
class RepositoryDetailsLoadingViewState(val loading: Boolean?) : RepositoryDetailsViewState()
class RepositoryDetailsErrorViewState(val error: Throwable?) : RepositoryDetailsViewState()
class RepositoryDetailsUrlViewState(val url: String?) : RepositoryDetailsViewState()