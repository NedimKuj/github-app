package com.nedkuj.github.di.fragment


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped

@InstallIn(FragmentComponent::class)
@Module
open class FragmentModule {
  @FragmentScoped
  @Provides
  fun navController(navigator: NavigatorImpl) : Navigator = navigator
}
