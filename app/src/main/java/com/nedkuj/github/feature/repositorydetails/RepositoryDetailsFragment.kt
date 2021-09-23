package com.nedkuj.github.feature.repositorydetails

import androidx.navigation.fragment.navArgs
import com.jakewharton.rxbinding3.view.clicks
import com.nedkuj.github.R
import com.nedkuj.github.common.BaseFragment
import com.nedkuj.github.databinding.FragmentRepositoryDetailsBinding
import com.nedkuj.github.model.Repository
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

    private val args: RepositoryDetailsFragmentArgs by navArgs()

    override fun initUI() {
        super.initUI()
        binding.context = this
    }

    override fun onLoad(): Observable<Repository> = RxLifecycle.onResume(this).map { args.repository }
    override fun onUserDetails(): Observable<Unit> = binding.ownerDetailsBtn.clicks()
    override fun onRepoDetails(): Observable<Unit> = binding.repoDetailsBtn.clicks()

    override fun render(viewState: RepositoryDetailsFullViewState) {
        bindingData.data.set(viewState)
    }

}