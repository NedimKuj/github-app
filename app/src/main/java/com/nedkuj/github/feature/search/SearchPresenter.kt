package com.nedkuj.github.feature.search

import android.content.Intent
import android.net.Uri
import com.nedkuj.github.BuildConfig
import com.nedkuj.github.common.BasePresenter
import com.nedkuj.github.di.fragment.Navigator
import com.nedkuj.github.model.SearchReposPayload
import com.nedkuj.github.network.repository.GetReposDataRepository
import io.reactivex.Observable
import javax.inject.Inject

class SearchPresenter @Inject constructor(
        private val navigator: Navigator,
        private val getReposDataRepository: GetReposDataRepository
) : BasePresenter<SearchView, SearchViewState, SearchFullViewState>() {
    override fun bindIntents() {
        val onSearch = intent(SearchView::onSearch)
                .switchMapToViewState(
                        { queryText ->
                            val payload = SearchReposPayload(1, 10, queryText)
                            getReposDataRepository.fetch(payload)
                        },
                        { SearchSuccessViewState(it.items) },
                        { throwable, _ -> SearchErrorViewState(throwable) },
                        { SearchLoadingViewState(true) }
                )

        val onRepo = intent(SearchView::onRepoClick)
                .switchMapToViewState(
                        { Observable.just(it) },
                        { SearchNavigationViewState(it) },
                        { throwable, _ -> SearchErrorViewState(throwable) },
                        { SearchLoadingViewState(true) }
                ).executeActionOn<SearchNavigationViewState> { state ->
                    state.parameter?.let { param ->
                        navigator.getNavController().navigate(SearchFragmentDirections.navSearchToRepoDetails(param))
                    }
                }.emmitAfter<SearchNavigationViewState> { SearchNavigationViewState(null) }

        val onUserImage = intent(SearchView::onUserImageClick)
                .switchMapToViewState(
                        { Observable.just(it) },
                        { SearchNavigationViewState(it) },
                        { throwable, _ -> SearchErrorViewState(throwable) },
                        { SearchLoadingViewState(true) }
                ).executeActionOn<SearchNavigationViewState> { state ->
                    state.parameter?.let { param ->
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(BuildConfig.WEB + param)
                        navigator.startActivityIntent(intent)
                    }
                }.emmitAfter<SearchNavigationViewState> { SearchNavigationViewState(null) }

        subscribeForViewStateChanges(onSearch, onRepo, onUserImage)
    }

    override fun viewStateReducer(previousState: SearchFullViewState, changes: SearchViewState): SearchFullViewState {
        return when (changes) {
            is SearchSuccessViewState -> previousState.copy(loading = false, repositories = changes.repositories)
            is SearchLoadingViewState -> previousState.copy(loading = changes.loading)
            is SearchErrorViewState -> previousState.copy(loading = false, error = changes.error)
            is SearchNavigationViewState -> previousState.copy(parameter = changes.parameter)
        }
    }

    override fun getInitialState(): SearchFullViewState = SearchFullViewState()
}