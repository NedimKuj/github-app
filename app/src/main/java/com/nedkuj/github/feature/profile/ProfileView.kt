package com.nedkuj.github.feature.profile

import androidx.lifecycle.Lifecycle
import com.nedkuj.github.common.BaseView
import io.reactivex.Observable

interface ProfileView : BaseView<ProfileFullViewState> {
    fun onLoad(): Observable<Lifecycle.Event>
}