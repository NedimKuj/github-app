package com.nedkuj.github.common

import android.os.Parcelable
import androidx.annotation.UiThread

interface Presenter<FVS : Parcelable> {
    var savedState: FVS?
    val latestViewState: FVS
    @UiThread
    fun attachView(view: BaseView<FVS>)
    @UiThread
    fun detachView()
    @UiThread
    fun destroy()
}