package com.vjettest.news.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment

abstract class AppBaseFragment : Fragment() {

    protected abstract val layoutId: Int

    open val supportToolbar: Toolbar? = null

    /**
     * View
     */

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutId, container, false)
    }


    protected fun <ViewT : View> bindView(@IdRes idRes: Int) = lazy(LazyThreadSafetyMode.NONE) {
        view!!.findViewById<ViewT>(idRes)!!
    }
}