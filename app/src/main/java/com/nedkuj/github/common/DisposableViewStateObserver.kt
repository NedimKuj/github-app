package com.nedkuj.github.common

import io.reactivex.observers.DisposableObserver
import io.reactivex.subjects.BehaviorSubject

internal class DisposableViewStateObserver<VS>(private val subject: BehaviorSubject<VS>) :
    DisposableObserver<VS>() {

    override fun onNext(value: VS) {
        subject.onNext(value)
    }

    override fun onError(e: Throwable) {
        throw IllegalStateException("ViewState observable must not reach error state - onError()", e)
    }

    override fun onComplete() {
    }
}
