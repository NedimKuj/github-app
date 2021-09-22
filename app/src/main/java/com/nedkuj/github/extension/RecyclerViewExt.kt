package com.nedkuj.github.extension

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

fun RecyclerView.onScrollListener(): Observable<Boolean> {
    lateinit var callback: RecyclerView.OnScrollListener
    return Observable.create<Boolean> { emitter ->
        callback = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    val layoutManager =
                        when (layoutManager) {
                            is GridLayoutManager -> layoutManager as GridLayoutManager
                            is LinearLayoutManager -> layoutManager as LinearLayoutManager
                            else -> null
                        }

                    if (layoutManager == null) {
                        emitter.onComplete()
                        throw IllegalArgumentException("Unsupported layout manager type")
                    }


                    if ((layoutManager.findLastCompletelyVisibleItemPosition() > recyclerView.adapter!!.itemCount - 10 ||
                        layoutManager.findLastCompletelyVisibleItemPosition() == -1) && recyclerView.adapter!!.itemCount > 10
                    ) {
                        emitter.onNext(true)
                    } else if (layoutManager.findLastCompletelyVisibleItemPosition() == recyclerView.adapter!!.itemCount ||
                        layoutManager.findLastCompletelyVisibleItemPosition() + 1 == recyclerView.adapter!!.itemCount
                    ) {
                        emitter.onNext(true)
                    }
                }
            }
        }
        this.addOnScrollListener(callback)
    }
        .throttleFirst(1000, TimeUnit.MILLISECONDS)
        .doOnDispose { this.removeOnScrollListener(callback) }
}