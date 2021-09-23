package com.nedkuj.github.feature.search

import android.content.Intent
import android.net.Uri
import com.nedkuj.github.BuildConfig
import com.nedkuj.github.common.BasePresenter
import com.nedkuj.github.di.fragment.Navigator
import com.nedkuj.github.model.SearchReposPayload
import com.nedkuj.github.model.SortState
import com.nedkuj.github.model.response.RepoResponseObject
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
                    if (queryText.isNotEmpty()) {
                        getReposDataRepository.fetch(
                            SearchReposPayload(1, BuildConfig.PAGINATION_LIMIT, queryText, latestViewState.sortState?.value)
                        )
                    } else {
                        Observable.just(RepoResponseObject(mutableListOf()))
                    }
                },
                { SearchSuccessViewState(it.items) },
                { throwable, _ -> SearchErrorViewState(throwable) },
                { SearchLoadingViewState(true) }
            )

        val onNextPage = intent(SearchView::onNextPage)
            .switchMapToViewState(
                { query ->
                    getReposDataRepository.fetch(
                        SearchReposPayload(
                            latestViewState.currentPage,
                            BuildConfig.PAGINATION_LIMIT,
                            query,
                            latestViewState.sortState?.value
                        )
                    )
                },
                { SearchMoreViewState(latestViewState.repositories.orEmpty().plus(it.items)) },
                { throwable, _ -> SearchErrorViewState(throwable) },
                { SearchMoreLoadingViewState(true, latestViewState.currentPage) }
            )

        val onSortClick = intent(SearchView::onSortClick)
            .switchMapToViewState(
                { Observable.just(it) },
                { SearchUrlViewState(null) },
                { throwable, _ -> SearchErrorViewState(throwable) }
            ).executeActionOn<SearchUrlViewState> {
                navigator.getNavController().navigate(SearchFragmentDirections.navSearchToSort())
            }

        val onSort = intent(SearchView::onSort)
            .switchMapToViewState(
                {
                    val queryText = it.first
                    val sortStateString = it.second ?: SortState.SORT_DEFAULT_BEST_MATCH.value

                    getReposDataRepository.fetch(
                        SearchReposPayload(1, BuildConfig.PAGINATION_LIMIT, queryText, sortStateString)
                    ).map { response ->
                        sortStateString to response
                    }
                },
                {
                    val sortStateString = it.first
                    val responseObject = it.second

                    SearchSortViewState(responseObject.items, SortState.from(sortStateString))
                },
                { throwable, _ -> SearchErrorViewState(throwable) },
                { SearchLoadingViewState(true) }
            ).emmitAfter<SearchSortViewState> { SearchMoreLoadingViewState(false, 1) }

        val onRepo = intent(SearchView::onRepoClick)
            .switchMapToViewState(
                { Observable.just(it) },
                { SearchNavigationViewState(it) },
                { throwable, _ -> SearchErrorViewState(throwable) },
                { SearchLoadingViewState(true) }
            ).executeActionOn<SearchNavigationViewState> { state ->
                state.repository?.let { param ->
                    navigator.getNavController().navigate(SearchFragmentDirections.navSearchToRepoDetails(param))
                }
            }.emmitAfter<SearchNavigationViewState> { SearchNavigationViewState(null) }

        val onUserImage = intent(SearchView::onUserImageClick)
            .switchMapToViewState(
                { Observable.just(it) },
                { SearchUrlViewState(it) },
                { throwable, _ -> SearchErrorViewState(throwable) },
                { SearchLoadingViewState(true) }
            ).executeActionOn<SearchUrlViewState> { state ->
                state.urlParameter?.let { param ->
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(BuildConfig.WEB + param)
                    navigator.startActivityIntent(intent)
                }
            }.emmitAfter<SearchUrlViewState> { SearchUrlViewState(null) }

        subscribeForViewStateChanges(
            onSearch,
            onNextPage,
            onSortClick,
            onSort,
            onRepo,
            onUserImage
        )
    }

    override fun viewStateReducer(previousState: SearchFullViewState, changes: SearchViewState): SearchFullViewState {
        return when (changes) {
            is SearchSuccessViewState -> previousState.copy(
                loading = false,
                repositories = changes.repositories,
                currentPage = 1
            )
            is SearchLoadingViewState -> previousState.copy(loading = changes.loading)
            is SearchErrorViewState -> previousState.copy(loading = false, error = changes.error)
            is SearchUrlViewState -> previousState.copy(urlParameter = changes.urlParameter)
            is SearchNavigationViewState -> previousState.copy(repository = changes.repository)
            is SearchMoreViewState -> previousState.copy(
                loading = false,
                repositories = changes.repositories
            )
            is SearchMoreLoadingViewState -> previousState.copy(loading = changes.loading, currentPage = changes.currentPage + 1)
            is SearchSortViewState -> previousState.copy(repositories = changes.repositories, sortState = changes.sortState)
        }
    }

    override fun getInitialState(): SearchFullViewState = SearchFullViewState()
}