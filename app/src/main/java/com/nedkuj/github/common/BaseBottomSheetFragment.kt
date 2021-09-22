package com.nedkuj.github.common

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.Settings
import android.transition.ChangeBounds
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.nedkuj.github.MainActivity
import com.nedkuj.github.R
import com.nedkuj.github.model.base.FragmentResultEntity
import com.nedkuj.github.util.rxlifecycle.RxLifecycle
import com.vanniktech.rxpermission.Permission

import io.reactivex.Observable

abstract class BaseBottomSheetFragment<FVS : Parcelable, VB : ViewDataBinding>(@LayoutRes private val layoutId: Int) :
    BottomSheetDialogFragment() {
    abstract val presenter: Presenter<FVS>?
    val bindingData = BindingData()
    protected lateinit var binding: VB
    private var snackbar: Snackbar? = null
    private val onActivityResult: Observable<FragmentResultEntity<Intent?>>
            by lazy {
                (activity as MainActivity).activityResultSubject.filter { it is FragmentResultEntity<*> && it.resultData is Intent? }
                    .map { it as FragmentResultEntity<Intent?> }
            }
    protected val resultObservable: Observable<FragmentResultEntity<Intent?>> by lazy {
        RxLifecycle.onResume(this)
            .flatMap { onActivityResult }
            .doOnNext { (activity as MainActivity).activityResultSubject.onNext(Unit) }
    }
    private val requestId: Int by ArgumentBinder("request_id", -1)

    private var resultData: Any? = null
    private val doNotAskAgain: (() -> Unit, Int) -> Unit = { onCancel, message ->
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(R.string.permission)
                .setCancelable(false)
                .setMessage(message)
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    onCancel.invoke()
                    dialog.dismiss()
                }
                .setPositiveButton(R.string.settings) { dialog, _ ->
                    val intent = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", activity?.packageName, null)
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    dialog.dismiss()
                }
                .show()
        }
    }

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter?.savedState = savedInstanceState?.getParcelable("state")
        retainInstance = true
    }

    final override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("state", presenter?.latestViewState)
    }

    open fun initUI() {
    }

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sharedElementEnterTransition = ChangeBounds().apply {
            duration = 750
        }
        sharedElementReturnTransition = ChangeBounds().apply {
            duration = 750
        }
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return binding.root
    }

    override fun onCreateAnimation(
        transit: Int,
        enter: Boolean,
        nextAnim: Int
    ): Animation? {
        var animation = super.onCreateAnimation(transit, enter, nextAnim)
        if (animation == null && nextAnim != 0) {
            animation = AnimationUtils.loadAnimation(activity, nextAnim)
        }
        if (animation != null) {
            view?.setLayerType(View.LAYER_TYPE_HARDWARE, null)
            animation.setAnimationListener(FragmentTransitionAnimationListener())
        }
        return animation
    }

    final override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    final override fun onStart() {
        super.onStart()
        presenter?.attachView(this as BaseView<FVS>)
    }

    final override fun onResume() {
        super.onResume()
    }

    final override fun onPause() {
        super.onPause()
    }

    final override fun onStop() {
        super.onStop()
        presenter?.detachView()
    }

    final override fun onDestroy() {
        super.onDestroy()
        if (activity?.isChangingConfigurations == false) {
            presenter?.destroy()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDetach() {
        super.onDetach()
        if (requestId != -1) {
            val fragmentResultEntity = FragmentResultEntity<Any?>()
            fragmentResultEntity.requestCode = requestId
            fragmentResultEntity.resultData = resultData
            fragmentResultEntity.resultCode =
                if (resultData == null)
                    FragmentResultEntity.CANCEL
                else
                    FragmentResultEntity.OK
            (activity as MainActivity).fragmentResultSubject
                .onNext(fragmentResultEntity)
        }
    }

    fun setResult(result: Any) {
        resultData = result
    }

    protected fun showError(error: Throwable?) {
        if (error == null) return
        val message = ""
        if (snackbar?.isShown == true && snackbar?.view?.findViewById<TextView>(
                R.id.snackbar_text
            )?.text?.toString() == message
        ) return
        snackbar = view?.let { Snackbar.make(it, message, Snackbar.LENGTH_LONG) }
        snackbar?.show()
    }

    protected fun showSuccess(message: String?) {
        if (message == null && snackbar?.isShown == true) {
            snackbar?.dismiss()
            return
        }

        if (message == null) {
            return
        }

        snackbar = view?.let { Snackbar.make(it, message, Snackbar.LENGTH_LONG) }
        snackbar?.show()
    }

    protected fun requestRequiredPermissions(
        vararg permissions: String,
        onDoNotAskAgainCancel: () -> Unit = {},
        onPermissionDenied: () -> Unit = {}
    ): Observable<Boolean> {
        return (activity as MainActivity).rxPermission
            .requestEach(*permissions)
            .buffer(permissions.size)
            .map {
                val deniedNotShown =
                    it.firstOrNull() { it.state() == Permission.State.DENIED_NOT_SHOWN }
                if (deniedNotShown != null) {
                    doNotAskAgain.invoke(onDoNotAskAgainCancel, R.string.lack_of_permissions)
                    false
                } else {
                    val allGranted = it.all { it.state() == Permission.State.GRANTED }
                    if (!allGranted) onPermissionDenied.invoke()
                    allGranted
                }
            }
            .filter { it }
    }

    private inner class FragmentTransitionAnimationListener : Animation.AnimationListener {
        override fun onAnimationRepeat(p0: Animation?) {
        }

        override fun onAnimationEnd(p0: Animation?) {
        }

        override fun onAnimationStart(p0: Animation?) {
            view?.setLayerType(View.LAYER_TYPE_NONE, null)
        }
    }

    inner class BindingData {
        val data: ObservableField<FVS> = ObservableField()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            val result = FragmentResultEntity<Intent?>()
            result.resultCode = resultCode
            result.resultData = data
            result.requestCode = requestCode
            (this.requireActivity() as MainActivity).activityResultSubject.onNext(result)
        }
    }
}















