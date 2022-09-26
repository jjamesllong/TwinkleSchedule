package com.keycome.twinkleschedule.util.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity
import com.keycome.twinkleschedule.base.BaseDialogFragment
import com.keycome.twinkleschedule.databinding.DialogDatePickerBinding
import com.keycome.twinkleschedule.extension.viewbindings.acquire
import com.keycome.twinkleschedule.record.interval.Date

abstract class DatePickerDialog : BaseDialogFragment() {

    private var _binding: DialogDatePickerBinding? = null
    val binding get() = _binding.acquire()

    var startDate: Date = Date(2000, 1, 1)
        private set

    var endDate: Date = Date(2100, 12, 31)
        private set

    var defaultDate: Date = Date.today()
        private set

    var selectedDate: Date? = null
        private set
        get() {
            val result = _binding?.let {
                val y = it.dialogDatePickerPicker.selectedYear
                val m = it.dialogDatePickerPicker.selectedMonth
                val d = it.dialogDatePickerPicker.selectedDay
                if (!(y == field?.year && m == field?.month && d == field?.dayOfMonth)) {
                    field = Date(y, m, d)
                }
                return@let field
            }
            return result
        }

    abstract fun configure()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = DialogDatePickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val date = savedInstanceState?.getString(
            KEY_DATE_SELECTED
        ) ?: arguments?.getString(
            KEY_DATE_SELECTED
        )
        date?.also { defaultDate = Date.fromString(it) }
        configure()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_DATE_SELECTED, defaultDate.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun onConfirm(action: (View) -> Unit) {
        binding.dialogDatePickerConfirm.setOnClickListener(action)
    }

    fun onCancel(action: (View) -> Unit) {
        binding.dialogDatePickerCancel.setOnClickListener(action)
    }

    fun dateRange(start: Date? = null, end: Date? = null, default: Date? = null) {
        val s = start?.let {
            startDate = it
            DateEntity.target(it.year, it.month, it.dayOfMonth)
        } ?: DateEntity.target(startDate.year, startDate.month, startDate.dayOfMonth)
        val e = end?.let {
            endDate = it
            DateEntity.target(it.year, it.month, it.dayOfMonth)
        } ?: DateEntity.target(endDate.year, endDate.month, endDate.dayOfMonth)
        val d = default?.let {
            defaultDate = it
            DateEntity.target(it.year, it.month, it.dayOfMonth)
        } ?: DateEntity.target(
            defaultDate.year, defaultDate.month, defaultDate.dayOfMonth
        )
        binding.dialogDatePickerPicker.setRange(s, e, d)
    }

    companion object {

        const val KEY_DATE_SELECTED = "date_picker_dialog_date_selected"
    }
}