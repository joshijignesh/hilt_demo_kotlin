package com.kisancalculator.base

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.kisancalculator.R
import com.kisancalculator.ui.progressBar.CustomProgressDialog
import com.kisancalculator.utils.Constants
import com.kisancalculator.utils.StartSettingsActivityContract
import com.kisancalculator.utils.Utils

abstract class BaseFragment<B : ViewDataBinding> : Fragment() {

    enum class ResultPermission {
        GRANTED_NFC_PERMISSION,
        GRANTED_CAMERA_PERMISSION
    }

    private var _progressBar: CustomProgressDialog? = null

    lateinit var mBinding: B

    //setting launch contract
    private val _cameraSettingLaunch =
        registerForActivityResult(StartSettingsActivityContract()) {
            onPermissionGrant(ResultPermission.GRANTED_CAMERA_PERMISSION)
        }

    //Permission Contract Launcher
    private val _permissionLaunch =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { perms ->
            val granted = perms.entries.all {
                it.value == true
            }
            if (granted) {
                if (perms.values == Constants.CAMERA_PERMISSIONS)
                    onPermissionGrant(ResultPermission.GRANTED_CAMERA_PERMISSION)
            } else {
                openSettingDialog()
            }

        }

    abstract fun layoutId(): Int
    abstract fun onViewCreated()
    abstract fun viewModelObservers()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate<B>(inflater, layoutId(), container, false)
        onViewCreated()
        return mBinding.root
    }

    protected open fun onPermissionGrant(permission: ResultPermission) {
        //perform permission operation
    }

    private fun openSettingDialog() {
        openDialog(requireActivity(),
            getString(R.string.lbl_access_permission_denied),
            getString(R.string.lbl_access_permission_denied_msg),
            getString(R.string.lbl_enable_feature),
            isCancelable = false
        ) {
            if (it == 0) {
                //perform setting launch
                _cameraSettingLaunch.launch(1)
            }
        }
    }

    protected fun openDialog(
        context: Context,
        title: String,
        msg: String,
        positiveButton: String,
        negativeButton: String = "",
        isCancelable: Boolean = true,
        listener: ((Int) -> Unit),
    ) {
        lifecycleScope.launchWhenResumed {
            Utils.openDialog(
                context, title, msg,
                positiveButton, negativeButton,
                isCancelable, listener
            )
        }
    }

    protected fun showToast(msg:String){
        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
    }

    fun showSnackBar(message: String) {
        if (::mBinding.isInitialized && mBinding.root != null) {
            val snackBar = Snackbar.make(mBinding.root, message, Snackbar.LENGTH_LONG)
            snackBar.setActionTextColor(
                ContextCompat.getColor(requireContext(),
                android.R.color.white))
            val sbView = snackBar.view
            sbView.setBackgroundColor(ContextCompat.getColor(requireContext(),
                android.R.color.black))
            val textView = sbView.findViewById<TextView>(R.id.snackbar_text)
            textView.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
            snackBar.show()
        }
    }

    /**
     * @param actionID : Set action means where to redirect
     * @param popUpID : Screen id which is remove from stack
     */
    protected fun navigateWithClearTop(actionID: Int, popUpID: Int) {
        try {
            findNavController().navigate(
                actionID, null, NavOptions.Builder().setPopUpTo(
                    popUpID, true
                ).build()
            )
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    protected fun navigateWithClearTop(directions: NavDirections, popUpID: Int) {
        findNavController().navigate(
            directions, NavOptions.Builder().setPopUpTo(
                popUpID, true
            ).build()
        )
    }

    protected fun navigate(actionID: Int, args: Bundle? = null) {
        try {
            findNavController().navigate(actionID, args)
        } catch (e: IllegalArgumentException) {
            e.localizedMessage
        }
    }

    protected fun navigate(directions: NavDirections) {
        findNavController().navigate(directions)
    }

    protected fun navigate(directions: NavDirections, args: Navigator.Extras) {
        findNavController().navigate(directions, args)
    }

    //camera permission
    protected fun hasPermissions(context: Context, permissions: Array<String>): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

    /** All About checking the permission */
    protected fun processPermission(permissionList: Array<String>) {
        var atLeastOnePermDenied = false
        var atLeastOnePermAsDontAskAgain = false
        permissionList.forEach {
            atLeastOnePermDenied = atLeastOnePermDenied || checkPermDenied(it)
            atLeastOnePermAsDontAskAgain = atLeastOnePermAsDontAskAgain || checkPermDontAskAgain(it)
        }
        if (atLeastOnePermDenied) {
            _permissionLaunch.launch(permissionList)
            return
        }
        if (atLeastOnePermAsDontAskAgain) {
            openSettingDialog()
            return
        }
        Toast.makeText(requireActivity(),
            ">>>  Execute your target action!!  <<<",
            Toast.LENGTH_LONG).show()
    }

    private fun checkPermDenied(perm: String): Boolean {
        return (ActivityCompat.checkSelfPermission(requireContext(), perm) ==
                PackageManager.PERMISSION_DENIED)
    }

    private fun checkPermDontAskAgain(perm: String): Boolean {
        return checkPermDenied(perm) && !shouldShowRequestPermissionRationale(perm)
    }

    /**
     * For progress dialog
     * */
    private fun showProgress() = _progressBar?.show("")
    private fun hideProgressDialog() = _progressBar?.hideDialog()
    protected fun disProgressDialog() = _progressBar?.dismissDialog()

    protected fun handleShowHideLoader(isShow: Boolean) {
        if (isShow) showProgress() else hideProgressDialog()
    }

    override fun onDestroy() {
        hideProgressDialog()
        super.onDestroy()
    }

    protected fun showBottomSheet(
        fragment: BottomSheetDialogFragment,
        bundle: Bundle = bundleOf(),
        isCancelable: Boolean = true,
    ) {
        fragment.isCancelable = isCancelable
        fragment.arguments = bundle
        fragment.show(childFragmentManager, fragment::class.java.simpleName)
    }

    /*fun openCustomDialog(
        context: Context,
        viewListener: ((DialogProductInfoBinding, AlertDialog) -> Unit),
    ) {
        val builder = AlertDialog.Builder(context, R.style.CustomDialogTheme)
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_product_info, mBinding.root as ViewGroup, false)
        val binding = DialogProductInfoBinding.bind(dialogView)
        builder.setView(binding.root)
        val alertDialog = builder.create()
        alertDialog.show()
        alertDialog.setOnDismissListener {
        }
        viewListener.invoke(binding, alertDialog)
    }

    fun onNetworkError(){
        openCustomDialog(requireContext()){ binding, dialog ->
            binding.apply {
                tvTitle.text = getString(R.string.lbl_no_internet_connection)
                tvDescription.text = getString(R.string.lbl_no_internet_connection_desc)
                tvBtn.text = getString(R.string.lbl_try_again)
                tvBtn.setOnClickListener { dialog.dismiss() }
            }
        }
    }*/

}