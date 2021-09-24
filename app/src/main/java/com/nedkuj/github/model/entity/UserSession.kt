package com.nedkuj.github.model.entity

import com.nedkuj.github.model.response.LoginResponse
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import javax.inject.Inject

open class UserSession @Inject constructor() : RealmObject() {

    constructor(loginResponse: LoginResponse) : this() {
        this.token = loginResponse.access_token
    }

    @PrimaryKey
    var id: Long = 1
    var token: String = ""

}
