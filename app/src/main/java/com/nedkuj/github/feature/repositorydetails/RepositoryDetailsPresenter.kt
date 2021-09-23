package com.nedkuj.github.feature.repositorydetails

import android.content.Intent
import android.net.Uri
import com.nedkuj.github.BuildConfig
import com.nedkuj.github.common.BasePresenter
import com.nedkuj.github.di.fragment.Navigator
import io.reactivex.Observable
import javax.inject.Inject

class RepositoryDetailsPresenter @Inject constructor(
    private val navigator: Navigator
) : BasePresenter<RepositoryDetailsView, RepositoryDetailsViewState, RepositoryDetailsFullViewState>() {
    override fun bindIntents() {
        val onLoad = intent(RepositoryDetailsView::onLoad)
            .switchMapToViewState(
                { Observable.just(it) },
                { RepositoryDetailsSuccessViewState(it) },
                { throwable, _ -> RepositoryDetailsErrorViewState(throwable) },
                { RepositoryDetailsLoadingViewState(true) }
            )

        val onRepoDetails = intent(RepositoryDetailsView::onRepoDetails)
            .switchMapToViewState(
                { Observable.just(it) },
                { RepositoryDetailsUrlViewState(latestViewState.repository?.full_name) },
                { throwable, _ -> RepositoryDetailsErrorViewState(throwable) },
                { RepositoryDetailsLoadingViewState(true) }
            ).executeActionOn<RepositoryDetailsUrlViewState> { state ->
                state.url?.let { param ->
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(BuildConfig.WEB + param)
                    navigator.startActivityIntent(intent)
                }
            }.emmitAfter<RepositoryDetailsUrlViewState> { RepositoryDetailsUrlViewState(null) }

        val onUserDetails = intent(RepositoryDetailsView::onUserDetails)
            .switchMapToViewState(
                { Observable.just(it) },
                { RepositoryDetailsUrlViewState(latestViewState.repository?.owner?.login) },
                { throwable, _ -> RepositoryDetailsErrorViewState(throwable) },
                { RepositoryDetailsLoadingViewState(true) }
            ).executeActionOn<RepositoryDetailsUrlViewState> { state ->
                state.url?.let { param ->
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(BuildConfig.WEB + param)
                    navigator.startActivityIntent(intent)
                }
            }.emmitAfter<RepositoryDetailsUrlViewState> { RepositoryDetailsUrlViewState(null) }

        subscribeForViewStateChanges(onLoad, onRepoDetails, onUserDetails)
    }

    override fun viewStateReducer(
        previousState: RepositoryDetailsFullViewState,
        changes: RepositoryDetailsViewState
    ): RepositoryDetailsFullViewState {
        return when (changes) {
            is RepositoryDetailsSuccessViewState -> previousState.copy(loading = false, repository = changes.repository)
            is RepositoryDetailsLoadingViewState -> previousState.copy(loading = changes.loading)
            is RepositoryDetailsErrorViewState -> previousState.copy(loading = false, error = changes.error)
            is RepositoryDetailsUrlViewState -> previousState.copy(url = changes.url)
        }
    }

    override fun getInitialState(): RepositoryDetailsFullViewState = RepositoryDetailsFullViewState()
}