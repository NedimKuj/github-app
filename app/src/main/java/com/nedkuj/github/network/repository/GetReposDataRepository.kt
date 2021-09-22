package com.nedkuj.github.network.repository

import com.nedkuj.github.model.SearchReposPayload
import com.nedkuj.github.model.response.ResponseObject
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GetReposDataRepository @Inject constructor() : DataRepository<SearchReposPayload, ResponseObject>() {
    override fun fetchData(payload: SearchReposPayload): Observable<ResponseObject> {
        return retrofitClient
                .getGitHubAPI()
                .getRepositories(payload.page, payload.perPage, payload.query, payload.sort)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
    }
}