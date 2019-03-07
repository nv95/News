package com.vjettest.news.common

import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity

abstract class AppBaseActivity : AppCompatActivity() {

    protected fun <ViewT : View> bindView(@IdRes idRes: Int) = lazy(LazyThreadSafetyMode.NONE) {
        findViewById<ViewT>(idRes)
    }
}