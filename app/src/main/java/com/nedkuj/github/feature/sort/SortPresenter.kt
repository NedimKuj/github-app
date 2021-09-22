package com.nedkuj.github.feature.sort

import com.nedkuj.github.common.BasePresenter
import com.nedkuj.github.model.SortState
import io.reactivex.Observable
import javax.inject.Inject

class SortPresenter @Inject constructor() : BasePresenter<SortView, SortViewState, SortFullViewState>() {
    override fun bindIntents() {
        val onStars = intent(SortView::onStars)
            .switchMapToViewState(
                { Observable.just(it) },
                { SortSuccessViewState(SortState.SORT_STARS)},
                { throwable, _ -> SortErrorViewState(throwable) }
            )

        val onForks = intent(SortView::onForks)
            .switchMapToViewState(
                { Observable.just(it) },
                { SortSuccessViewState(SortState.SORT_FORKS)},
                { throwable, _ -> SortErrorViewState(throwable) }
            )

        val onUpdated = intent(SortView::onUpdated)
            .switchMapToViewState(
                { Observable.just(it) },
                { SortSuccessViewState(SortState.SORT_UPDATED)},
                { throwable, _ -> SortErrorViewState(throwable) }
            )

        val onDefault = intent(SortView::onDefault)
            .switchMapToViewState(
                { Observable.just(it) },
                { SortSuccessViewState(SortState.SORT_DEFAULT_BEST_MATCH)},
                { throwable, _ -> SortErrorViewState(throwable) }
            )

        subscribeForViewStateChanges(onStars, onForks, onUpdated, onDefault)
    }

    override fun viewStateReducer(previousState: SortFullViewState, changes: SortViewState): SortFullViewState {
        return when (changes) {
            is SortSuccessViewState -> previousState.copy(loading = false, sortState = changes.sortState)
            is SortLoadingViewState -> previousState.copy(loading = changes.loading)
            is SortErrorViewState -> previousState.copy(loading = false, error = changes.error)
        }
    }

    override fun getInitialState(): SortFullViewState = SortFullViewState()
}