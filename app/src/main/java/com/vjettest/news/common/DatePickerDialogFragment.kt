package com.vjettest.news.common

import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.appcompat.app.AlertDialog
import com.vjettest.news.R
import java.util.*

class DatePickerDialogFragment : AppBaseDialogFragment<Date?>() {

    override val layoutId = R.layout.dialog_datepicker

    private val datePicker by bindView<DatePicker>(R.id.datePicker)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val calendar = Calendar.getInstance()
        datePicker.init(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
            null
        )
    }

    override fun onBuildDialog(dialog: AlertDialog.Builder) {
        dialog.setNeutralButton(R.string.reset) { _,_ ->
            listener?.onDialogConfirmed(arguments, null)
        }
    }

    override fun getData(): Date? = Calendar.getInstance().apply {
        set(
            datePicker.year,
            datePicker.month,
            datePicker.dayOfMonth
        )
    }.time
}