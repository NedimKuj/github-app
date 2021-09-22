package com.nedkuj.github.feature.sort

import androidx.lifecycle.Lifecycle
import com.nedkuj.github.common.BaseView
import io.reactivex.Observable

interface SortView : BaseView<SortFullViewState> {
    fun onLoad(): Observable<Lifecycle.Event>
    fun onStars(): Observable<Unit>
    fun onForks(): Observable<Unit>
    fun onUpdated(): Observable<Unit>
    fun onDefault(): Observable<Unit>
}