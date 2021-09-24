package com.nedkuj.github.feature.profile

import androidx.lifecycle.Lifecycle
import com.nedkuj.github.R
import com.nedkuj.github.common.BaseFragment
import com.nedkuj.github.databinding.FragmentProfileBinding
import com.nedkuj.github.util.rxlifecycle.RxLifecycle
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BaseFragment<ProfileFullViewState, FragmentProfileBinding>(R.layout.fragment_profile), ProfileView {

    @Inject
    override lateinit var presenter: ProfilePresenter

    override fun initUI() {
        super.initUI()
        binding.context = this
    }

    override fun onLoad(): Observable<Lifecycle.Event> = RxLifecycle.onResume(this)

    override fun render(viewState: ProfileFullViewState) {
        bindingData.data.set(viewState)
    }
}