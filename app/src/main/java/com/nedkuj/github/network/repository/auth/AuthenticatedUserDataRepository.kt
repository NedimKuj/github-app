package com.nedkuj.github.network.repository.auth

import com.nedkuj.github.model.AuthenticatedUser
import com.nedkuj.github.network.repository.DataRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AuthenticatedUserDataRepository @Inject constructor() : DataRepository<Any, AuthenticatedUser>() {
    override fun fetchData(payload: Any): Observable<AuthenticatedUser> {
        return retrofitClient
            .getGitHubAPI()
            .getAuthenticatedUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .toObservable()
    }
}