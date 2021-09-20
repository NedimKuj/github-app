package com.nedkuj.github.common

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.transition.ChangeBounds
import com.google.android.material.snackbar.Snackbar
import com.nedkuj.github.R
import com.nedkuj.github.extension.getErrorMessageStringRes
import com.nedkuj.github.extension.toErrorMessage
import com.nedkuj.github.model.base.FragmentResultEntity
import com.nedkuj.github.model.base.PermissionResultEntity
import com.nedkuj.github.util.rxlifecycle.RxLifecycle
import com.vanniktech.rxpermission.Permission
import com.vanniktech.rxpermission.RealRxPermission
import io.reactivex.Observable

abstract class BaseDialogFragment<FVS : Parcelable, VB : ViewDataBinding>(@LayoutRes private val layoutId: Int) :
    DialogFragment() {
    val bindingData = BindingData()
    abstract val presenter: Presenter<FVS>?
    private var snackbar: Snackbar? = null
    private var activityResultEntity: FragmentResultEntity<Intent>? = null
    private var activityResultEntity2: FragmentResultEntity<Intent>? = null
    private val listOfPermissionRequests: MutableList<PermissionResultEntity> = mutableListOf()
    private var shouldStartWithPermission: Boolean = true
    protected val leanplumParams = mutableMapOf<String, Any>()
    protected lateinit var binding: VB

    protected var resultObservable: Observable<FragmentResultEntity<Intent>> =
        RxLifecycle.onResume(this)
            .filter { activityResultEntity2 != null }
            .map {
                val result = activityResultEntity2!!
                activityResultEntity2 = null
                result
            }

    private var resultData: Any? = null

    private val doNotAskAgain: () -> Unit = {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(R.string.lack_of_permissions)
                .setMessage(R.string.lack_of_permissions)
                .setNegativeButton(R.string.cancel) { dialog, _ ->
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
                ?.show()
        }
    }

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        presenter?.savedState = savedInstanceState?.getParcelable("state")
    }

    open fun initUI() {
    }

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedElementEnterTransition = ChangeBounds().apply {
            duration = 750
        }
        sharedElementReturnTransition = ChangeBounds().apply {
            duration = 750
        }
        //showLoader(false)
        isCancelable = false
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
        //showLoader(false)
        initUI()
    }

    final override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("state", presenter?.latestViewState)
    }

    final override fun onStart() {
        super.onStart()
    }

    final override fun onResume() {
        super.onResume()
        presenter?.attachView(this as BaseView<FVS>)
        listOfPermissionRequests.forEach {
            val granted = it.permissions.all {
                RealRxPermission.getInstance(context)
                    .isGranted(it)
            }
            if (granted && shouldStartWithPermission) {
                //(activity as MainActivity).permissionResultProcessor.onNext(it.copy(result = granted))
            }
        }

        if (activityResultEntity != null) {
            //(activity as MainActivity).fragmentResultProcessor.onNext(activityResultEntity)
            activityResultEntity = null
        }
    }

    override fun onPause() {
        super.onPause()
        presenter?.detachView()
    }

    final override fun onStop() {
        super.onStop()
    }

    final override fun onDestroy() {
        super.onDestroy()
        if (activity?.isChangingConfigurations == false) {
            presenter?.destroy()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        snackbar?.dismiss()
        hideKeyboard(requireActivity())
    }

    override fun onDetach() {
        super.onDetach()

        val requestId = arguments?.get("request_id") as Int?
        if (requestId!=null && requestId != -1) {
            val fragmentResultEntity = FragmentResultEntity<Any?>()
            fragmentResultEntity.requestCode = requestId
            fragmentResultEntity.resultData = resultData
            fragmentResultEntity.resultCode =
                if (resultData == null)
                    FragmentResultEntity.CANCEL
                else
                    FragmentResultEntity.OK
        }
    }

    fun setResult(result: Any) {
        resultData = result
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

    protected fun showToastError(error: Throwable?) {
        if (error == null) return

        val message = resources.getString(error.toErrorMessage().getErrorMessageStringRes())
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        //showLoader(false)
    }

    protected fun showError(messageKey: Throwable?) {
        if (messageKey == null) return

        val message = resources.getString(messageKey.toErrorMessage().getErrorMessageStringRes())
        snackbar = view?.let { Snackbar.make(it, message, Snackbar.LENGTH_LONG) }
        snackbar?.show()
        //showLoader(false)
    }

    protected fun reemmitPermission(shouldEmmit: Boolean) {
        this.shouldStartWithPermission = shouldEmmit
    }

    protected fun requestRequiredPermissions(
        vararg permissions: String,
        permissionRequestCode: Int,
        onDoNotAskAgain: () -> Unit = doNotAskAgain
    ): Observable<Boolean> {
        listOfPermissionRequests.add(
            PermissionResultEntity(
                permissions.toList(),
                permissionRequestCode
            )
        )
        return RealRxPermission.getInstance(context)
            .requestEach(*permissions)
            .buffer(permissions.size)
            .map {
                if (it.any { it.state() == Permission.State.DENIED_NOT_SHOWN }) {
                    onDoNotAskAgain.invoke()
                    false
                } else {
                    it.all { it.state() == Permission.State.GRANTED }
                }
            }
            .filter { it }
            .doOnNext {
                listOfPermissionRequests.remove(
                    PermissionResultEntity(
                        permissions.toList(),
                        permissionRequestCode
                    )
                )
                // (activity as MainActivity).permissionResultProcessor.onNext(
                //   PermissionResultEntity(permissions.toList(), permissionRequestCode, it)
                // )
            }
            .flatMap { Observable.empty<Boolean>() }
    }

//    protected fun showLoader(show: Boolean?) {
//        if (activity is StartActivity) {
//            if (show != null && show) {
//                (activity as StartActivity).setLoading(true)
//            } else {
//                (activity as StartActivity).setLoading(false)
//            }
//        }
//    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
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

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        activityResultEntity = FragmentResultEntity()
        activityResultEntity?.requestCode = requestCode
        activityResultEntity?.resultCode = resultCode
        activityResultEntity?.resultData = data

        activityResultEntity2 = FragmentResultEntity()
        activityResultEntity2?.requestCode = requestCode
        activityResultEntity2?.resultCode = resultCode
        activityResultEntity2?.resultData = data
    }

    inner class BindingData {
        val data: ObservableField<FVS> = ObservableField()
    }

//    val mainComponent: FragmentComponent
//        get() {
//            return DaggerFragmentComponent.builder()
//                .activityComponent((activity as BaseActivity).activityComponent)
//                .fragmentModule(FragmentModule(this))
//                .build()
//        }
}