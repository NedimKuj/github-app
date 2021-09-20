package com.nedkuj.github.common.permission

import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.M
import androidx.annotation.RequiresApi
import com.nedkuj.github.common.permission.IRxPermission
import com.nedkuj.github.MainActivity
import com.vanniktech.rxpermission.Permission
import dagger.hilt.android.qualifiers.ActivityContext
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class RxPermission @Inject constructor(@ActivityContext val context: Context) : IRxPermission {
    // Contains all the current permission requests. Once granted or denied, they are removed from it.
    private val currentPermissionRequests = HashMap<String, PublishSubject<Permission>>()
    private val TRIGGER = Any()
    internal val isMarshmallow: Boolean
        get() = SDK_INT >= M

    /**
     * Requests permissions immediately, **must be invoked during initialization phase of your application**.
     */
    override fun requestEach(vararg permissions: String): Observable<Permission> {
        return Observable.just(TRIGGER)
            .compose(ensureEach<Any>(*permissions))
    }

    /**
     * Requests the permission immediately, **must be invoked during initialization phase of your application**.
     */
    override fun request(permission: String): Single<Permission> {
        return requestEach(permission)
            .firstOrError()
    }

    /**
     * Map emitted items from the source observable into [Permission] objects for each
     * permission in parameters.
     *
     *
     * If one or several permissions have never been requested, invoke the related framework method
     * to ask the user if he allows the permissions.
     */
    private fun <T> ensureEach(vararg permissions: String): ObservableTransformer<T, Permission> {
        checkPermissions(permissions)
        return ObservableTransformer { o -> request(o, *permissions) }
    }

    internal fun request(
        trigger: Observable<*>,
        vararg permissions: String
    ): Observable<Permission> {
        return Observable.merge(trigger, pending(*permissions))
            .flatMap { requestOnM(*permissions) }
    }

    private fun pending(vararg permissions: String): Observable<*> {
        for (p in permissions) {
            if (!currentPermissionRequests.containsKey(p)) {
                return Observable.empty<Any>()
            }
        }
        return Observable.just(TRIGGER)
    }

    @TargetApi(M)
    internal fun requestOnM(vararg permissions: String): Observable<Permission> {
        val list = ArrayList<Observable<Permission>>(permissions.size)
        val unrequestedPermissions = ArrayList<String>()
        // In case of multiple permissions, we create an observable for each of them.
        // At the end, the observables are combined to have a unique response.
        for (permission in permissions) {
            if (isGranted(permission)) {
                list.add(Observable.just(Permission.granted(permission)))
            } else if (isRevokedByPolicy(permission)) {
                list.add(Observable.just(Permission.revokedByPolicy(permission)))
            } else {
                var subject: PublishSubject<Permission>? = currentPermissionRequests[permission]
                // Create a new subject if not exists
                if (subject == null) {
                    unrequestedPermissions.add(permission)
                    subject = PublishSubject.create()
                    currentPermissionRequests[permission] = subject
                }
                list.add(subject)
            }
        }
        if (!unrequestedPermissions.isEmpty()) {
            val permissionsToRequest = unrequestedPermissions.toTypedArray()
            startShadowActivity(permissionsToRequest)
        }
        return Observable.concat(Observable.fromIterable(list))
    }

    /**
     * Returns true if the permission is already granted.
     *
     *
     * Always true if SDK &lt; 23.
     */
    override fun isGranted(permission: String): Boolean {
        return !isMarshmallow || isGrantedOnM(permission)
    }

    /**
     * Returns true if the permission has been revoked by a policy.
     *
     *
     * Always false if SDK &lt; 23.
     */
    override fun isRevokedByPolicy(permission: String): Boolean {
        return isMarshmallow && isRevokedOnM(permission)
    }

    @TargetApi(M)
    private fun isGrantedOnM(permission: String): Boolean {
        return context.checkSelfPermission(permission) == PERMISSION_GRANTED
    }

    @TargetApi(M)
    private fun isRevokedOnM(permission: String): Boolean {
        return context.packageManager
            .isPermissionRevokedByPolicy(permission, context.packageName)
    }

    @RequiresApi(M)
    internal fun startShadowActivity(permissions: Array<String>) {
        (context as? MainActivity)?.handlePermissions(permissions)
    }

    override fun onRequestPermissionsResult(
        grantResults: IntArray,
        rationale: BooleanArray,
        rationaleAfter: BooleanArray,
        vararg permissions: String
    ) {
        val size = permissions.size
        for (i in 0 until size) {
            val subject = currentPermissionRequests[permissions[i]]
                ?: throw IllegalStateException(
                    "RealRxPermission.onRequestPermissionsResult invoked but didn't find the corresponding permission request."
                )
            currentPermissionRequests.remove(permissions[i])
            val granted = grantResults[i] == PERMISSION_GRANTED
            if (granted) {
                subject.onNext(Permission.granted(permissions[i]))
            } else if (!rationale[i] && !rationaleAfter[i]) {
                subject.onNext(Permission.deniedNotShown(permissions[i]))
            } else {
                subject.onNext(Permission.denied(permissions[i]))
            }
            subject.onComplete()
        }
    }

    fun checkPermissions(permissions: Array<out String>) {
        require(permissions.isNotEmpty()) { "permissions are null or empty" }
    }
}