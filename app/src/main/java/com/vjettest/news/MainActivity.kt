package com.vjettest.news

import android.os.Bundle
import com.vjettest.news.common.AppBaseActivity

class MainActivity : AppBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
