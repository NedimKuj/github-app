package com.nedkuj.github.network.session

import com.nedkuj.github.model.entity.UserSession
import com.nedkuj.github.model.response.LoginResponse
import com.nedkuj.github.network.Interceptor
import io.reactivex.Observable
import io.realm.Realm
import javax.inject.Inject

class SessionImpl @Inject constructor(
    private val interceptor: Interceptor
) : Session {

    override fun create(loginResponse: LoginResponse): Observable<Unit> {
        return Observable.fromCallable {
            interceptor.token = loginResponse.access_token
            Realm.getDefaultInstance().executeTransaction {
                it.copyToRealmOrUpdate(UserSession(loginResponse))
            }
        }
    }

    override fun clear(): Observable<Unit> {
        return Observable.fromCallable {
            interceptor.token = ""
            Realm.getDefaultInstance().executeTransaction {
                it.where(UserSession::class.java).findAll().deleteAllFromRealm()
            }
        }
    }

    override fun get(): Observable<UserSession> {
        return Observable.fromCallable {
            Realm.getDefaultInstance().where(UserSession::class.java).findFirst()
        }
    }

    override fun hasSession(): Observable<Boolean> {
        return Observable.fromCallable {
            Realm.getDefaultInstance().where(UserSession::class.java).findFirst() != null
        }
    }
}
