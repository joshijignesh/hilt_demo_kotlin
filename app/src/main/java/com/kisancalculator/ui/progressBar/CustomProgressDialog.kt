package com.kisancalculator.ui.progressBar


import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.widget.LinearLayout
import com.kisancalculator.R
import com.wang.avi.AVLoadingIndicatorView

class CustomProgressDialog : Dialog {
    private var _context: Context? = null
    private var _dialog: CustomProgressDialog? = null
    private var _avi: AVLoadingIndicatorView? = null

    constructor(context: Context?) : super(context!!) {
        _context = context
    }

    constructor(context: Context?, theme: Int) : super(context!!, theme)

    fun show(message: CharSequence?): CustomProgressDialog {
        _dialog = CustomProgressDialog(_context, R.style.ProgressDialog)
        _dialog?.setContentView(R.layout.dialog_custom_progressbar)
        _avi = _dialog?.findViewById(R.id.avi)
        _context?.let { _avi?.show() }
        _dialog!!.setCancelable(false)
        _dialog?.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        _dialog!!.window!!.attributes.gravity = Gravity.CENTER
        val lp = _dialog!!.window!!.attributes
        lp.dimAmount = 0.0f
        _dialog!!.window!!.attributes = lp

        if (_dialog != null && _dialog?.isShowing == false) {
            _dialog!!.show()
        }
        return this._dialog!!
    }

    fun hideDialog(): CustomProgressDialog? {
        if (_dialog != null) {
            _dialog?._context?.let { _avi?.hide() }
            _dialog!!.hide()
        }
        return _dialog
    }

    fun dismissDialog(): CustomProgressDialog? {
        return try {
            _dialog?.dismiss()
            _dialog?._context?.let { _avi?.hide() }
            _dialog
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
