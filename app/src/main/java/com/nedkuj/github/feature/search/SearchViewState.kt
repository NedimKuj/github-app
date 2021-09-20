package com.nedkuj.github.feature.search

import android.os.Parcelable
import android.widget.SearchView
import kotlinx.parcelize.Parcelize

sealed class SearchViewState

@Parcelize
data class SearchFullViewState(
    val loading: Boolean? = null,
    val error: Throwable? = null
) : Parcelable

class SearchSuccessViewState(): SearchViewState()
class SearchLoadingViewState(val loading: Boolean?): SearchViewState()
class SearchErrorViewState(val error: Throwable?): SearchViewState()