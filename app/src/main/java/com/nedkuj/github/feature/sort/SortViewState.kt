package com.nedkuj.github.feature.sort

import android.os.Parcelable
import com.nedkuj.github.model.SortState
import kotlinx.android.parcel.Parcelize

sealed class SortViewState

@Parcelize
data class SortFullViewState(
    val loading: Boolean? = null,
    val error: Throwable? = null,
    val sortState: SortState? = null
) : Parcelable

class SortSuccessViewState(val sortState: SortState?) : SortViewState()
class SortLoadingViewState(val loading: Boolean?) : SortViewState()
class SortErrorViewState(val error: Throwable?) : SortViewState()
