package com.nedkuj.github.di.activity

import android.content.Context
import com.nedkuj.github.common.permission.IRxPermission
import com.nedkuj.github.common.permission.RxPermission
import com.nedkuj.github.model.base.FragmentResultEntity
import com.nedkuj.github.model.base.PermissionResultEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import io.reactivex.processors.PublishProcessor
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

@InstallIn(ActivityComponent::class)
@Module
class ActivityModule {
    @ActivityScoped
    @Provides
    @FragmentResult
    fun providesFragmentResultProcessor(): PublishProcessor<FragmentResultEntity<*>> = PublishProcessor.create()

    @ActivityScoped
    @Provides
    @RxPermissionResult
    fun providesRxPermissionResult(): PublishProcessor<PermissionResultEntity> = PublishProcessor.create()

    @Provides
    fun providesRxPermission(@ApplicationContext context: Context): IRxPermission = RxPermission(context)

    @FragmentResult
    @Provides
    fun providesFragmentResultSubject(): PublishSubject<FragmentResultEntity<*>> = PublishSubject.create()

    @ActivityResult
    @Provides
    fun providesActivityResultSubject(): BehaviorSubject<Any> = BehaviorSubject.create()
}
