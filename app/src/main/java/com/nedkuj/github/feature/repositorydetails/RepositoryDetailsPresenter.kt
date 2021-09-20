package com.nedkuj.github.feature.repositorydetails

import com.nedkuj.github.common.BasePresenter
import io.reactivex.Observable
import javax.inject.Inject

class RepositoryDetailsPresenter @Inject constructor() :
    BasePresenter<RepositoryDetailsView, RepositoryDetailsViewState, RepositoryDetailsFullViewState>() {
    override fun bindIntents() {
        val onLoad = intent(RepositoryDetailsView::onLoad)
            .switchMapToViewState(
                { Observable.just(it) },
                { RepositoryDetailsSuccessViewState() },
                { throwable, _ -> RepositoryDetailsErrorViewState(throwable) },
                { RepositoryDetailsLoadingViewState(true) }
            )

        subscribeForViewStateChanges(onLoad)
    }

    override fun viewStateReducer(
        previousState: RepositoryDetailsFullViewState,
        changes: RepositoryDetailsViewState
    ): RepositoryDetailsFullViewState {
        return when (changes) {
            is RepositoryDetailsSuccessViewState -> previousState.copy(loading = false)
            is RepositoryDetailsLoadingViewState -> previousState.copy(loading = changes.loading)
            is RepositoryDetailsErrorViewState -> previousState.copy(loading = false, error = changes.error)
        }
    }

    override fun getInitialState(): RepositoryDetailsFullViewState = RepositoryDetailsFullViewState()
}