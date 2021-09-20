package com.nedkuj.github.feature.repositorydetails

import androidx.lifecycle.Lifecycle
import com.nedkuj.github.common.BaseView
import io.reactivex.Observable

interface RepositoryDetailsView : BaseView<RepositoryDetailsFullViewState> {
    fun onLoad(): Observable<Lifecycle.Event>
}