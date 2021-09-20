package com.nedkuj.github.feature.repositorydetails

import androidx.lifecycle.Lifecycle
import com.nedkuj.github.R
import com.nedkuj.github.common.BaseFragment
import com.nedkuj.github.databinding.FragmentRepositoryDetailsBinding
import com.nedkuj.github.util.rxlifecycle.RxLifecycle
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import javax.inject.Inject

@AndroidEntryPoint
class RepositoryDetailsFragment :
    BaseFragment<RepositoryDetailsFullViewState, FragmentRepositoryDetailsBinding>(R.layout.fragment_repository_details),
    RepositoryDetailsView {

    @Inject
    override lateinit var presenter: RepositoryDetailsPresenter

    override fun initUI() {
        super.initUI()
        binding.context = this
    }

    override fun onLoad(): Observable<Lifecycle.Event> = RxLifecycle.onResume(this)

    override fun render(viewState: RepositoryDetailsFullViewState) {
        bindingData.data.set(viewState)
    }

}