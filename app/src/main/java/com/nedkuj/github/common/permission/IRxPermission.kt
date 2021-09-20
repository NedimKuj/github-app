package com.nedkuj.github.common.permission

import com.vanniktech.rxpermission.Permission
import io.reactivex.Observable
import io.reactivex.Single

interface IRxPermission {

    /** Requests a single permission.  */
    fun request(permission: String): Single<Permission>

    /** Requests multiple permissions.  */
    fun requestEach(vararg permissions: String): Observable<Permission>

    /** Returns true when the given permission is granted.  */
    fun isGranted(permission: String): Boolean

    /** Returns true when the given permission is revoked by a policy.  */

    fun isRevokedByPolicy(permission: String): Boolean

    fun onRequestPermissionsResult(
        grantResults: IntArray,
        rationale: BooleanArray,
        rationaleAfter: BooleanArray,
        vararg permissions: String
    )
}