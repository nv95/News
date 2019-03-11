package com.vjettest.news.common

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment

abstract class AppBaseDialogFragment<T> : AppCompatDialogFragment() {

    protected abstract val layoutId: Int

    protected var listener: DialogListener<T>? = null

    /**
     * View
     */

    private val dialogView: View by lazy {
        LayoutInflater.from(requireContext()).inflate(layoutId, null, false)
    }

    protected fun <ViewT : View> bindView(@IdRes idRes: Int) = lazy(LazyThreadSafetyMode.NONE) {
        dialogView.findViewById<ViewT>(idRes)!!
    }

    /**
     * Callback
     */

    abstract fun getData(): T

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        @Suppress("UNCHECKED_CAST")
        listener = activity as? DialogListener<T>
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext(), theme)
            .setView(dialogView)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                listener?.onDialogConfirmed(arguments, getData())
            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->
                listener?.onDialogCancelled(arguments)
            }
            .also {
                onBuildDialog(it)
            }
            .create()
    }

    open fun onBuildDialog(dialog: AlertDialog.Builder) {
    }

    interface DialogListener<T> {
        fun onDialogConfirmed(args: Bundle?, data: T)
        fun onDialogCancelled(args: Bundle?)
    }
}