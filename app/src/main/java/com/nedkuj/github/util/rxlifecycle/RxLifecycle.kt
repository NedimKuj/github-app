package com.nedkuj.github.util.rxlifecycle

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.reactivestreams.Publisher
import org.reactivestreams.Subscription

class RxLifecycle(lifecycle: Lifecycle) {
    private val subject = PublishSubject.create<Lifecycle.Event>().toSerialized()
    private val observer: RxLifecycleObserver
    private val lifecycle: Lifecycle
    fun onEvent(): Observable<Lifecycle.Event> {
        return subject
    }

    fun onCreate(): Observable<Lifecycle.Event> {
        return onEvent().filter { event -> Lifecycle.Event.ON_CREATE == event }
    }

    fun onStart(): Observable<Lifecycle.Event> {
        return onEvent().filter { event -> Lifecycle.Event.ON_START == event }
    }

    fun onResume(): Observable<Lifecycle.Event> {
        return onEvent().filter { event -> Lifecycle.Event.ON_RESUME == event }
    }

    fun onPause(): Observable<Lifecycle.Event> {
        return onEvent().filter { event -> Lifecycle.Event.ON_PAUSE == event }
    }

    fun onStop(): Observable<Lifecycle.Event> {
        return onEvent().filter { event -> Lifecycle.Event.ON_STOP == event }
    }

    fun onDestroy(): Observable<Lifecycle.Event> {
        return onEvent().filter { event -> Lifecycle.Event.ON_DESTROY == event }
    }

    fun onAny(): Observable<Lifecycle.Event> {
        return onEvent().filter { event -> Lifecycle.Event.ON_ANY == event }
    }

    fun <T> onlyIfResumedOrStarted(value: T): Observable<T> {
        return Observable.just(lifecycle)
            .flatMap { lifecycle ->
                val currentState = lifecycle.currentState
                if (currentState == Lifecycle.State.RESUMED || currentState == Lifecycle.State.STARTED) {
                    Observable.just(value)
                } else {
                    onResume()
                        .map { value }
                }
            }
    }

    fun disposeOnDestroy(disposable: Disposable) {
        onDestroy()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { disposable.dispose() }
    }

    fun disposeOnStop(disposable: Disposable) {
        onStop()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { disposable.dispose() }
    }

    fun disposeOnPause(disposable: Disposable) {
        onPause()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { disposable.dispose() }
    }

    fun <T> disposeOnDestroy(): RxTransformer<T, T> {
        return object : RxTransformer<T, T>() {
            override fun apply(upstream: Flowable<T>): Publisher<T> {
                return upstream.doOnSubscribe { subscription -> disposeOnDestroy(subscription) }
            }

            override fun apply(upstream: Completable): CompletableSource {
                return upstream.doOnSubscribe { disposable -> disposeOnDestroy(disposable) }
            }

            override fun apply(upstream: Single<T>): SingleSource<T> {
                return upstream.doOnSubscribe { disposable -> disposeOnDestroy(disposable) }
            }

            override fun apply(upstream: Maybe<T>): MaybeSource<T> {
                return upstream.doOnSubscribe { disposable -> disposeOnDestroy(disposable) }
            }

            override fun apply(upstream: Observable<T>): ObservableSource<T> {
                return upstream.doOnSubscribe { disposable -> disposeOnDestroy(disposable) }
            }
        }
    }

    fun <T> disposeOnPause(): RxTransformer<T, T> {
        return object : RxTransformer<T, T>() {
            override fun apply(upstream: Flowable<T>): Publisher<T> {
                return upstream.doOnSubscribe { subscription -> disposeOnPause(subscription) }
            }

            override fun apply(upstream: Completable): CompletableSource {
                return upstream.doOnSubscribe { disposable -> disposeOnPause(disposable) }
            }

            override fun apply(upstream: Single<T>): SingleSource<T> {
                return upstream.doOnSubscribe { disposable -> disposeOnPause(disposable) }
            }

            override fun apply(upstream: Maybe<T>): MaybeSource<T> {
                return upstream.doOnSubscribe { disposable -> disposeOnPause(disposable) }
            }

            override fun apply(upstream: Observable<T>): ObservableSource<T> {
                return upstream.doOnSubscribe { disposable -> disposeOnPause(disposable) }
            }
        }
    }

    fun <T> disposeOnStop(): RxTransformer<T, T> {
        return object : RxTransformer<T, T>() {
            override fun apply(upstream: Flowable<T>): Publisher<T> {
                return upstream.doOnSubscribe { subscription -> disposeOnStop(subscription) }
            }

            override fun apply(upstream: Completable): CompletableSource {
                return upstream.doOnSubscribe { disposable -> disposeOnStop(disposable) }
            }

            override fun apply(upstream: Single<T>): SingleSource<T> {
                return upstream.doOnSubscribe { disposable -> disposeOnStop(disposable) }
            }

            override fun apply(upstream: Maybe<T>): MaybeSource<T> {
                return upstream.doOnSubscribe { disposable -> disposeOnStop(disposable) }
            }

            override fun apply(upstream: Observable<T>): ObservableSource<T> {
                return upstream.doOnSubscribe { disposable -> disposeOnStop(disposable) }
            }
        }
    }

    fun <T> disposeOn(disposeEvent: DISPOSE_EVENT?): RxTransformer<T, T> {
        return object : RxTransformer<T, T>() {
            override fun apply(upstream: Flowable<T>): Publisher<T> {
                return upstream.doOnSubscribe { disposeOn<Any>(disposeEvent) }
            }

            override fun apply(upstream: Completable): CompletableSource {
                return upstream.doOnSubscribe { disposeOn<Any>(disposeEvent) }
            }

            override fun apply(upstream: Single<T>): SingleSource<T> {
                return upstream.doOnSubscribe { disposeOn<Any>(disposeEvent) }
            }

            override fun apply(upstream: Maybe<T>): MaybeSource<T> {
                return upstream.doOnSubscribe { disposeOn<Any>(disposeEvent) }
            }

            override fun apply(upstream: Observable<T>): ObservableSource<T> {
                return upstream.doOnSubscribe { disposeOn<Any>(disposeEvent) }
            }
        }
    }

    fun disposeOnDestroy(subscription: Subscription) {
        onDestroy()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { subscription.cancel() }
    }

    fun disposeOnStop(subscription: Subscription) {
        onStop()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { subscription.cancel() }
    }

    fun disposeOnPause(subscription: Subscription) {
        onPause()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { subscription.cancel() }
    }

    abstract inner class RxTransformer<U, D> : ObservableTransformer<U, D>, SingleTransformer<U, D>, MaybeTransformer<U, D>, CompletableTransformer, FlowableTransformer<U, D>
    enum class DISPOSE_EVENT {
        DESTROY, STOP, PAUSE
    }

    companion object {
        fun with(lifecycleOwner: LifecycleOwner): RxLifecycle {
            return RxLifecycle(lifecycleOwner.lifecycle)
        }

        fun with(lifecycle: Lifecycle): RxLifecycle {
            return RxLifecycle(lifecycle)
        }

        fun with(lifecycleActivity: AppCompatActivity): RxLifecycle {
            return RxLifecycle(lifecycleActivity.lifecycle)
        }

        fun with(lifecycleFragment: Fragment): RxLifecycle {
            return RxLifecycle(lifecycleFragment.lifecycle)
        }

        fun onEvent(lifecycle: Lifecycle): Observable<Lifecycle.Event> {
            return with(lifecycle).onEvent()
        }

        fun onEvent(lifecycle: LifecycleOwner): Observable<Lifecycle.Event> {
            return with(lifecycle).onEvent()
        }

        fun onCreate(lifecycle: Lifecycle): Observable<Lifecycle.Event> {
            return with(lifecycle).onCreate()
        }

        fun onCreate(lifecycle: LifecycleOwner): Observable<Lifecycle.Event> {
            return with(lifecycle).onCreate()
        }

        fun onStart(lifecycle: Lifecycle): Observable<Lifecycle.Event> {
            return with(lifecycle).onStart()
        }

        fun onStart(lifecycle: LifecycleOwner): Observable<Lifecycle.Event> {
            return with(lifecycle).onStart()
        }

        fun onResume(lifecycle: Lifecycle): Observable<Lifecycle.Event> {
            return with(lifecycle).onResume()
        }

        fun onResume(lifecycle: LifecycleOwner): Observable<Lifecycle.Event> {
            return with(lifecycle).onResume()
        }

        fun onPause(lifecycle: Lifecycle): Observable<Lifecycle.Event> {
            return with(lifecycle).onPause()
        }

        fun onPause(lifecycle: LifecycleOwner): Observable<Lifecycle.Event> {
            return with(lifecycle).onPause()
        }

        fun onStop(lifecycle: Lifecycle): Observable<Lifecycle.Event> {
            return with(lifecycle).onStop()
        }

        fun onStop(lifecycle: LifecycleOwner): Observable<Lifecycle.Event> {
            return with(lifecycle).onStop()
        }

        fun onDestroy(lifecycle: Lifecycle): Observable<Lifecycle.Event> {
            return with(lifecycle).onDestroy()
        }

        fun onDestroy(lifecycle: LifecycleOwner): Observable<Lifecycle.Event> {
            return with(lifecycle).onDestroy()
        }

        fun onAny(lifecycle: Lifecycle): Observable<Lifecycle.Event> {
            return with(lifecycle).onAny()
        }

        fun onAny(lifecycle: LifecycleOwner): Observable<Lifecycle.Event> {
            return with(lifecycle).onAny()
        }

        fun <T> onlyIfResumedOrStarted(lifecycleOwner: LifecycleOwner, value: T): Observable<T> {
            return with(lifecycleOwner).onlyIfResumedOrStarted(value)
        }

        fun <T> onlyIfResumedOrStarted(lifecycle: Lifecycle, value: T): Observable<T> {
            return with(lifecycle).onlyIfResumedOrStarted(value)
        }

        fun disposeOnDestroy(lifecycleOwner: LifecycleOwner, disposable: Disposable) {
            with(lifecycleOwner).disposeOnDestroy(disposable)
        }

        fun disposeOnDestroy(lifecycle: Lifecycle, disposable: Disposable) {
            with(lifecycle).disposeOnDestroy(disposable)
        }

        fun disposeOn(lifecycle: Lifecycle, disposeEvent: DISPOSE_EVENT?, disposable: Disposable) {
            when (disposeEvent) {
                DISPOSE_EVENT.STOP -> disposeOnStop(lifecycle, disposable)
                DISPOSE_EVENT.PAUSE -> disposeOnPause(lifecycle, disposable)
                DISPOSE_EVENT.DESTROY -> disposeOnDestroy(lifecycle, disposable)
            }
        }

        fun disposeOn(lifecycleOwner: LifecycleOwner, disposeEvent: DISPOSE_EVENT?, disposable: Disposable) {
            disposeOn(lifecycleOwner.lifecycle, disposeEvent, disposable)
        }

        fun disposeOnDestroy(lifecycleOwner: LifecycleOwner, subscription: Subscription) {
            with(lifecycleOwner).disposeOnDestroy(subscription)
        }

        fun disposeOnDestroy(lifecycle: Lifecycle, subscription: Subscription) {
            with(lifecycle).disposeOnDestroy(subscription)
        }

        fun disposeOnStop(lifecycleOwner: LifecycleOwner, disposable: Disposable) {
            with(lifecycleOwner).disposeOnStop(disposable)
        }

        fun disposeOnStop(lifecycle: Lifecycle, disposable: Disposable) {
            with(lifecycle).disposeOnStop(disposable)
        }

        fun disposeOnStop(lifecycleOwner: LifecycleOwner, subscription: Subscription) {
            with(lifecycleOwner).disposeOnStop(subscription)
        }

        fun disposeOnStop(lifecycle: Lifecycle, subscription: Subscription) {
            with(lifecycle).disposeOnStop(subscription)
        }

        fun disposeOnPause(lifecycleOwner: LifecycleOwner, disposable: Disposable) {
            with(lifecycleOwner).disposeOnPause(disposable)
        }

        fun disposeOnPause(lifecycle: Lifecycle, disposable: Disposable) {
            with(lifecycle).disposeOnPause(disposable)
        }

        fun disposeOnPause(lifecycleOwner: LifecycleOwner, subscription: Subscription) {
            with(lifecycleOwner).disposeOnPause(subscription)
        }

        fun disposeOnPause(lifecycle: Lifecycle, subscription: Subscription) {
            with(lifecycle).disposeOnPause(subscription)
        }

        fun <T> disposeOn(lifecycle: Lifecycle, disposeEvent: DISPOSE_EVENT?): RxTransformer<T, T> {
            return with(lifecycle).disposeOn(disposeEvent)
        }

        fun <T> disposeOn(lifecycleOwner: LifecycleOwner, disposeEvent: DISPOSE_EVENT?): RxTransformer<T, T> {
            return disposeOn(lifecycleOwner.lifecycle, disposeEvent)
        }

        fun <T> disposeOnDestroy(lifecycle: Lifecycle): RxTransformer<T, T> {
            return with(lifecycle).disposeOnDestroy()
        }

        fun <T> disposeOnDestroy(lifecycleOwner: LifecycleOwner): RxTransformer<T, T> {
            return with(lifecycleOwner).disposeOnDestroy()
        }

        fun <T> disposeOnPause(lifecycle: Lifecycle): RxTransformer<T, T> {
            return with(lifecycle).disposeOnPause()
        }

        fun <T> disposeOnPause(lifecycleOwner: LifecycleOwner): RxTransformer<T, T> {
            return with(lifecycleOwner).disposeOnPause()
        }

        fun <T> disposeOnStop(lifecycle: Lifecycle): RxTransformer<T, T> {
            return with(lifecycle).disposeOnStop()
        }

        fun <T> disposeOnStop(lifecycleOwner: LifecycleOwner): RxTransformer<T, T> {
            return with(lifecycleOwner).disposeOnStop()
        }
    }

    init {
        observer = RxLifecycleObserver(subject)
        this.lifecycle = lifecycle
        lifecycle.addObserver(observer)
    }
}