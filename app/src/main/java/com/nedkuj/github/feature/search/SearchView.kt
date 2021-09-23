package com.nedkuj.github.feature.search

import androidx.lifecycle.Lifecycle
import com.nedkuj.github.common.BaseView
import com.nedkuj.github.model.Repository
import io.reactivex.Observable

interface SearchView : BaseView<SearchFullViewState> {
    fun onLoad(): Observable<Lifecycle.Event>
    fun onNextPage(): Observable<String>
    fun onSearch(): Observable<String>
    fun onRepoClick(): Observable<Repository>
    fun onUserImageClick(): Observable<String>
    fun onSortClick(): Observable<Unit>
    fun onSort(): Observable<Pair<String, String?>>
}