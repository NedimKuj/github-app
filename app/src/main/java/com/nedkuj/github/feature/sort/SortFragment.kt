package com.nedkuj.github.feature.sort

import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Lifecycle
import com.jakewharton.rxbinding3.view.clicks
import com.nedkuj.github.R
import com.nedkuj.github.common.BaseBottomSheetFragment
import com.nedkuj.github.databinding.FragmentSortBinding
import com.nedkuj.github.model.SortState
import com.nedkuj.github.util.rxlifecycle.RxLifecycle
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import javax.inject.Inject

@AndroidEntryPoint
class SortFragment : BaseBottomSheetFragment<SortFullViewState, FragmentSortBinding>(R.layout.fragment_sort), SortView {

    @Inject
    override lateinit var presenter: SortPresenter

    override fun initUI() {
        super.initUI()
        binding.context = this
    }

    override fun onLoad(): Observable<Lifecycle.Event> = RxLifecycle.onResume(this)

    override fun onStars(): Observable<Unit> = binding.sortByStars.clicks()
    override fun onForks(): Observable<Unit> = binding.sortByForks.clicks()
    override fun onUpdated(): Observable<Unit> = binding.sortByUpdated.clicks()
    override fun onDefault(): Observable<Unit> = binding.sortByDefault.clicks()


    override fun render(viewState: SortFullViewState) {
        handleSort(viewState.sortState)
    }

    private fun handleSort(sortState: SortState?) {
        if (sortState == null) return
        setFragmentResult(SORT_KEY, bundleOf(SORT_KEY to sortState.value))
        dialog?.dismiss()
    }

    private companion object {
        private const val SORT_KEY = "sort"
    }
}