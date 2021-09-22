package com.nedkuj.github.extension

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import io.reactivex.Observable

fun Fragment.onResult(requestKey: String): Observable<Pair<String, Bundle>> {
    return Observable.create<Pair<String, Bundle>> { emitter ->
        val listener =
            { requestKey: String, bundle: Bundle -> emitter.onNext(requestKey to bundle) }

        listener.let {
            this.setFragmentResultListener(requestKey, listener)
        }
    }.doOnDispose {
        this.clearFragmentResultListener(requestKey)
    }
}