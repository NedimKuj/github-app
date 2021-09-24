package com.nedkuj.github.feature.auth

import com.nedkuj.github.common.BaseView
import io.reactivex.Observable

interface AuthView : BaseView<AuthFullViewState> {
    fun onLoad(): Observable<String>
}