package com.nedkuj.github.network.session

import com.nedkuj.github.model.entity.UserSession
import com.nedkuj.github.model.response.LoginResponse
import io.reactivex.Observable

interface Session {
    fun create(loginResponse: LoginResponse): Observable<Unit>
    fun clear(): Observable<Unit>
    fun get(): Observable<UserSession>
    fun hasSession(): Observable<Boolean>
}
