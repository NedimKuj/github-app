package com.nedkuj.github.feature.auth

import com.nedkuj.github.common.BasePresenter
import com.nedkuj.github.di.fragment.Navigator
import com.nedkuj.github.network.repository.auth.LoginDataRepository
import javax.inject.Inject

class AuthPresenter @Inject constructor(
    private val navigator: Navigator,
    private val loginDataRepository: LoginDataRepository
) : BasePresenter<AuthView, AuthViewState, AuthFullViewState>() {

    override fun bindIntents() {
        val onLoad = intent(AuthView::onLoad)
            .switchMapToViewState(
                { loginDataRepository.fetch(it) },
                { AuthSuccessViewState() },
                { throwable, _ -> AuthErrorViewState(throwable) }
            ).executeActionOn<AuthSuccessViewState> {
                navigator.getNavController().navigate(AuthFragmentDirections.navAuthToProfile())
            }

        subscribeForViewStateChanges(onLoad)
    }

    override fun viewStateReducer(previousState: AuthFullViewState, changes: AuthViewState): AuthFullViewState {
        return when (changes) {
            is AuthSuccessViewState -> previousState.copy(loading = false)
            is AuthLoadingViewState -> previousState.copy(loading = changes.loading)
            is AuthErrorViewState -> previousState.copy(loading = false, error = changes.error)
        }
    }

    override fun getInitialState(): AuthFullViewState = AuthFullViewState()
}