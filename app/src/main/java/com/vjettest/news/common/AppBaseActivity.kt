package com.vjettest.news.common

import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.vjettest.news.R

abstract class AppBaseActivity : AppCompatActivity() {

    protected open val toolbar by bindView<Toolbar>(R.id.toolbar)

    protected fun <ViewT : View> bindView(@IdRes idRes: Int) = lazy(LazyThreadSafetyMode.NONE) {
        findViewById<ViewT>(idRes)
    }
}