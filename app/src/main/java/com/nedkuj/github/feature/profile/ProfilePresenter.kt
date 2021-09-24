package com.nedkuj.github.feature.profile

import com.nedkuj.github.common.BasePresenter
import com.nedkuj.github.network.repository.auth.AuthenticatedUserDataRepository
import javax.inject.Inject

class ProfilePresenter @Inject constructor(
    private val authenticatedUserDataRepository: AuthenticatedUserDataRepository
) : BasePresenter<ProfileView, ProfileViewState, ProfileFullViewState>() {
    override fun bindIntents() {
        val onLoad = intent(ProfileView::onLoad)
            .switchMapToViewState(
                { authenticatedUserDataRepository.fetch(Unit) },
                { ProfileSuccessViewState(it) },
                { throwable, _ -> ProfileErrorViewState(throwable) },
                { ProfileLoadingViewState(true) }
            )

        subscribeForViewStateChanges(onLoad)
    }

    override fun viewStateReducer(previousState: ProfileFullViewState, changes: ProfileViewState): ProfileFullViewState {
        return when (changes) {
            is ProfileSuccessViewState -> previousState.copy(loading = false, user = changes.user)
            is ProfileLoadingViewState -> previousState.copy(loading = changes.loading)
            is ProfileErrorViewState -> previousState.copy(loading = false, error = changes.error)
        }
    }

    override fun getInitialState(): ProfileFullViewState = ProfileFullViewState()
}