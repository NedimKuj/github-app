package com.nedkuj.github.feature.search

import androidx.lifecycle.Lifecycle
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import com.nedkuj.github.R
import com.nedkuj.github.common.BaseFragment
import com.nedkuj.github.databinding.FragmentSearchBinding
import com.nedkuj.github.extension.onResult
import com.nedkuj.github.extension.onScrollListener
import com.nedkuj.github.feature.search.adapter.SearchAdapter
import com.nedkuj.github.util.rxlifecycle.RxLifecycle
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : BaseFragment<SearchFullViewState, FragmentSearchBinding>(R.layout.fragment_search), SearchView {

    @Inject
    override lateinit var presenter: SearchPresenter

    @Inject
    lateinit var searchAdapter: SearchAdapter

    override fun initUI() {
        super.initUI()
        binding.context = this
        binding.repositoriesRw.adapter = searchAdapter
    }

    override fun onLoad(): Observable<Lifecycle.Event> = RxLifecycle.onResume(this)
    override fun onSearch(): Observable<String> = binding.searchField.textChanges()
        .debounce(SEARCH_DEBOUNCE_IN_MILLIS, TimeUnit.MILLISECONDS)
        .map { it.toString() }

    override fun onNextPage(): Observable<String> = binding.repositoriesRw.onScrollListener()
        .filter { it && binding.searchField.text.toString().isNotEmpty() }
        .map { binding.searchField.text.toString() }

    override fun onRepoClick(): Observable<String> = searchAdapter.onItemPressed
    override fun onUserImageClick(): Observable<String> = searchAdapter.onUserImagePressed

    override fun onSortClick(): Observable<Unit> = binding.sortIcon.clicks()
    override fun onSort(): Observable<Pair<String, String?>> = onResult(SORT_KEY).share()
        .filter { binding.searchField.text.toString().isNotEmpty() }
        .map { binding.searchField.text.toString() to it.second.getString(SORT_KEY) }

    override fun render(viewState: SearchFullViewState) {
        bindingData.data.set(viewState)
    }

    private companion object {
        private const val SEARCH_DEBOUNCE_IN_MILLIS = 2000L
        private const val SORT_KEY = "sort"
    }

}