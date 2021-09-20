package com.nedkuj.github

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.nedkuj.github.common.permission.IRxPermission
import com.nedkuj.github.di.activity.ActivityResult
import com.nedkuj.github.di.activity.FragmentResult
import com.nedkuj.github.model.base.FragmentResultEntity
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.annotations.NonNull
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE = 42

    @Inject
    lateinit var rxPermission: IRxPermission

    @Inject
    @ActivityResult
    lateinit var activityResultSubject: BehaviorSubject<Any>

    @Inject
    @FragmentResult
    lateinit var fragmentResultSubject: PublishSubject<FragmentResultEntity<*>>

    private var orientation: Int = 0

    private lateinit var shouldShowRequestPermissionRationale: BooleanArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        orientation = resources.configuration.orientation
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        @NonNull permissions: Array<String>,
        @NonNull grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE) {
            val rationales = rationales(permissions)
            rxPermission.onRequestPermissionsResult(
                grantResults, shouldShowRequestPermissionRationale, rationales,
                *permissions
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun rationales(@NonNull permissions: Array<String>): BooleanArray {
        val rationales = BooleanArray(permissions.size)

        for (i in permissions.indices) {
            rationales[i] = shouldShowRequestPermissionRationale(permissions[i])
        }
        return rationales
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun handlePermissions(permissions: Array<String>) {
        shouldShowRequestPermissionRationale = rationales(permissions)
        requestPermissions(permissions, REQUEST_CODE)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (orientation != newConfig.orientation) {
            supportFragmentManager.apply {
                fragments.forEach { frag ->
                    beginTransaction()
                        .detach(frag)
                        .addToBackStack(frag.id.toString())
                        .commit()
                    popBackStackImmediate()
                }
            }
            orientation = newConfig.orientation
        }
    }
}