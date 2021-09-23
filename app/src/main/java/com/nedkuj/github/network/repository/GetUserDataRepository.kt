package com.nedkuj.github.network.repository

import com.nedkuj.github.model.Owner
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GetUserDataRepository @Inject constructor() : DataRepository<String, Owner>() {
    override fun fetchData(payload: String): Observable<Owner> {
        return retrofitClient
            .getGitHubAPI()
            .getUser(payload)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .toObservable()
    }
}