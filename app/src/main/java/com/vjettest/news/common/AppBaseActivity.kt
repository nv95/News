package com.vjettest.news.common

import android.view.MenuItem
import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.vjettest.news.R
import java.net.UnknownHostException

abstract class AppBaseActivity : AppCompatActivity() {

    protected open val toolbar: Toolbar? by bindView(R.id.toolbar)

    protected fun <ViewT : View> bindView(@IdRes idRes: Int) = lazy(LazyThreadSafetyMode.NONE) {
        findViewById<ViewT>(idRes)
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when(item?.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}