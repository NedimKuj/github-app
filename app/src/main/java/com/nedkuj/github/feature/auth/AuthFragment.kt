package com.nedkuj.github.feature.auth

import androidx.navigation.fragment.navArgs
import com.nedkuj.github.R
import com.nedkuj.github.common.BaseFragment
import com.nedkuj.github.databinding.FragmentAuthBinding
import com.nedkuj.github.util.rxlifecycle.RxLifecycle
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import javax.inject.Inject

@AndroidEntryPoint
class AuthFragment : BaseFragment<AuthFullViewState, FragmentAuthBinding>(R.layout.fragment_auth), AuthView {

    @Inject
    override lateinit var presenter: AuthPresenter

    private val args: AuthFragmentArgs by navArgs()

    override fun initUI() {
        super.initUI()
        binding.context = this
    }

    override fun onLoad(): Observable<String> = RxLifecycle.onResume(this).map { args.code }

    override fun render(viewState: AuthFullViewState) {
        bindingData.data.set(viewState)
    }
}