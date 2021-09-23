package com.nedkuj.github.feature.repositorydetails

import com.nedkuj.github.common.BaseView
import com.nedkuj.github.model.Repository
import io.reactivex.Observable

interface RepositoryDetailsView : BaseView<RepositoryDetailsFullViewState> {
    fun onLoad(): Observable<Repository>
    fun onRepoDetails(): Observable<Unit>
    fun onUserDetails(): Observable<Unit>
}