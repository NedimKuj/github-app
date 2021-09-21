package com.nedkuj.github.feature.search

import androidx.lifecycle.Lifecycle
import com.nedkuj.github.R
import com.nedkuj.github.common.BaseFragment
import com.nedkuj.github.databinding.FragmentSearchBinding
import com.nedkuj.github.feature.search.adapter.SearchAdapter
import com.nedkuj.github.util.rxlifecycle.RxLifecycle
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
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
    override fun onSearch(): Observable<String> = RxLifecycle.onResume(this).map { "a" }

    override fun onRepoClick(): Observable<String> = searchAdapter.onItemPressed
    override fun onUserImageClick(): Observable<String> = searchAdapter.onUserImagePressed

    override fun render(viewState: SearchFullViewState) {
        bindingData.data.set(viewState)
    }

}