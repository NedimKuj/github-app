package com.nedkuj.github.feature.search

import androidx.lifecycle.Lifecycle
import com.nedkuj.github.common.BaseView
import io.reactivex.Observable

interface SearchView : BaseView<SearchFullViewState> {
    fun onLoad(): Observable<Lifecycle.Event>
}