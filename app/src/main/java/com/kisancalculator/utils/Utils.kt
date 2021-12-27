package com.kisancalculator.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog

class Utils {
    companion object {
        fun openDialog(
            context: Context,
            title: String,
            msg: String,
            positiveButton: String,
            negativeButton: String = "",
            isCancelable: Boolean = true,
            listener: ((Int) -> Unit),
        ){
            val positiveAction = 0
            val negativeAction = 1

            val builder = AlertDialog.Builder(context)
            builder.setCancelable(isCancelable)
            builder.setTitle(title)
            builder.setMessage(msg)
            builder.setPositiveButton(
                positiveButton
            ) { dialog, _ ->
                dialog.cancel()
                listener.invoke(positiveAction  )
            }
            builder.setNegativeButton(negativeButton) { dialog, _ ->
                dialog.cancel()
                listener.invoke(negativeAction)
            }
            builder.show()
        }
    }
}