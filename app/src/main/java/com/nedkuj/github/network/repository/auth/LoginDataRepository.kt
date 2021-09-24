package com.nedkuj.github.network.repository.auth

import android.content.Context
import android.os.Build
import com.nedkuj.github.BuildConfig
import com.nedkuj.github.model.response.LoginResponse
import com.nedkuj.github.network.repository.DataRepository
import com.nedkuj.github.network.session.Session
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class LoginDataRepository @Inject constructor(
    val session: Session,
    @ApplicationContext val context: Context
) :
    DataRepository<String, LoginResponse>() {
    override fun fetchData(payload: String): Observable<LoginResponse> {
        return retrofitClient
            .getGitHubAuthAPI()
            .getAccessToken(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET, payload)
            .flatMapObservable { loginresponse ->
                session.create(loginresponse).map {
                    loginresponse
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}