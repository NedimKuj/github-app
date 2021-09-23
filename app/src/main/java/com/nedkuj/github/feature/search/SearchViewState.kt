package com.nedkuj.github.feature.search

import android.os.Parcelable
import com.nedkuj.github.model.Repository
import com.nedkuj.github.model.SortState
import kotlinx.parcelize.Parcelize

sealed class SearchViewState

@Parcelize
data class SearchFullViewState(
    val loading: Boolean? = null,
    val error: Throwable? = null,
    val repositories: List<Repository>? = null,
    val parameter: String? = null,
    val currentPage: Int = 1,
    val sortState: SortState? = null
) : Parcelable

class SearchSuccessViewState(val repositories: List<Repository>?) : SearchViewState()
class SearchLoadingViewState(val loading: Boolean?) : SearchViewState()
class SearchErrorViewState(val error: Throwable?) : SearchViewState()
class SearchNavigationViewState(val parameter: String?) : SearchViewState()
class SearchMoreViewState(val repositories: List<Repository>?): SearchViewState()
class SearchMoreLoadingViewState(val loading: Boolean?, val currentPage: Int): SearchViewState()
class SearchSortViewState(val repositories: List<Repository>?, val sortState: SortState?): SearchViewState()