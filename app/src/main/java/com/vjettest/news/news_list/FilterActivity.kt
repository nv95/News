package com.vjettest.news.news_list

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.vjettest.news.R
import com.vjettest.news.common.AppBaseActivity
import com.vjettest.news.common.AppBaseDialogFragment
import com.vjettest.news.common.DatePickerDialogFragment
import com.vjettest.news.common.format
import com.vjettest.news.core.network.options.EverythingRequestOptions
import java.util.*

class FilterActivity : AppBaseActivity(), AppBaseDialogFragment.DialogListener<Date?> {

    private val buttonDateFrom by bindView<Button>(R.id.button_dateFrom)
    private val buttonDateTo by bindView<Button>(R.id.button_dateTo)

    private var dateFrom: Date? = null
    private var dateTo: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        buttonDateFrom.setOnClickListener(this::onClick)
        buttonDateTo.setOnClickListener(this::onClick)

        dateFrom = intent?.getSerializableExtra(DATEPICKER_TARGET_FROM) as? Date
        dateTo = intent?.getSerializableExtra(DATEPICKER_TARGET_TO) as? Date

        buttonDateFrom.text = dateFrom?.format(this) ?: getString(R.string.no_date_selected)
        buttonDateTo.text = dateTo?.format(this) ?: getString(R.string.no_date_selected)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_filter, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
        R.id.action_done -> {
            setResult(
                Activity.RESULT_OK, Intent().putExtras(
                    bundleOf(
                        DATEPICKER_TARGET_FROM to dateFrom,
                        DATEPICKER_TARGET_TO to dateTo
                    )
                )
            )
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun onClick(view: View) {
        when (view.id) {
            buttonDateFrom.id -> {
                DatePickerDialogFragment()
                    .apply {
                        arguments = bundleOf(DATEPICKER_KEY_TARGET to DATEPICKER_TARGET_FROM)
                    }
                    .show(supportFragmentManager, null)
            }
            buttonDateTo.id -> {
                DatePickerDialogFragment()
                    .apply {
                        arguments = bundleOf(DATEPICKER_KEY_TARGET to DATEPICKER_TARGET_TO)
                    }
                    .show(supportFragmentManager, null)
            }
        }
    }

    override fun onDialogConfirmed(args: Bundle?, data: Date?) {
        when (args?.getString(DATEPICKER_KEY_TARGET)) {
            DATEPICKER_TARGET_FROM -> {
                dateFrom = data
                buttonDateFrom.text = dateFrom?.format(this) ?: getString(R.string.no_date_selected)
            }
            DATEPICKER_TARGET_TO -> {
                dateTo = data
                buttonDateTo.text = dateTo?.format(this) ?: getString(R.string.no_date_selected)
            }
        }
    }

    override fun onDialogCancelled(args: Bundle?) = Unit

    companion object {

        private const val DATEPICKER_KEY_TARGET = "target"
        private const val DATEPICKER_TARGET_FROM = "from"
        private const val DATEPICKER_TARGET_TO = "to"

        fun open(fragment: Fragment, options: EverythingRequestOptions, requestCode: Int) {
            fragment.startActivityForResult(
                Intent(fragment.context, FilterActivity::class.java)
                    .putExtra(DATEPICKER_TARGET_FROM, options.from)
                    .putExtra(DATEPICKER_TARGET_TO, options.to),
                requestCode
            )
        }

        fun handleResult(intent: Intent, options: EverythingRequestOptions) {
            options.from = intent.getSerializableExtra(DATEPICKER_TARGET_FROM) as? Date
            options.to = intent.getSerializableExtra(DATEPICKER_TARGET_TO) as? Date
        }
    }
}