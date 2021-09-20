package com.nedkuj.github.feature.search

import com.nedkuj.github.common.BasePresenter
import io.reactivex.Observable
import javax.inject.Inject

class SearchPresenter @Inject constructor() : BasePresenter<SearchView, SearchViewState, SearchFullViewState>() {
    override fun bindIntents() {
        val onLoad = intent(SearchView::onLoad)
            .switchMapToViewState(
                { Observable.just(it) },
                { SearchSuccessViewState() },
                { throwable, _ -> SearchErrorViewState(throwable) },
                { SearchLoadingViewState(true) }
            )

        subscribeForViewStateChanges(onLoad)
    }

    override fun viewStateReducer(previousState: SearchFullViewState, changes: SearchViewState): SearchFullViewState {
        return when (changes) {
            is SearchSuccessViewState -> previousState.copy(loading = false)
            is SearchLoadingViewState -> previousState.copy(loading = changes.loading)
            is SearchErrorViewState -> previousState.copy(loading = false, error = changes.error)
        }
    }

    override fun getInitialState(): SearchFullViewState = SearchFullViewState()
}