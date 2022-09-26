package com.keycome.twinkleschedule.util.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.gzuliyujiang.wheelpicker.entity.TimeEntity
import com.keycome.twinkleschedule.base.BaseDialogFragment
import com.keycome.twinkleschedule.databinding.DialogTimePickerBinding
import com.keycome.twinkleschedule.extension.viewbindings.acquire
import com.keycome.twinkleschedule.record.interval.Time

abstract class TimePickerDialog : BaseDialogFragment() {

    private var _binding: DialogTimePickerBinding? = null
    val binding get() = _binding.acquire()

    var startTime: Time = Time(0, 0, 0)
        private set

    var endTime: Time = Time(23, 59, 59)
        private set

    var defaultTime: Time = Time.now()
        private set

    var selectedTime: Time? = null
        private set
        get() {
            val result = _binding?.let {
                val h = it.dialogTimePickerPicker.selectedHour
                val m = it.dialogTimePickerPicker.selectedMinute
                val s = it.dialogTimePickerPicker.selectedSecond
                if (!(h == field?.hour && m == field?.second && s == field?.second)) {
                    field = Time(h, m, s)
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
        _binding = DialogTimePickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val time = savedInstanceState?.getString(
            KEY_TIME_SELECTED
        ) ?: arguments?.getString(
            KEY_TIME_SELECTED
        )
        time?.also { defaultTime = Time.from(it) }
        configure()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        selectedTime?.also {
            outState.putString(KEY_TIME_SELECTED, it.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun onConfirm(action: (View) -> Unit) {
        binding.dialogTimePickerConfirm.setOnClickListener(action)
    }

    fun onCancel(action: (View) -> Unit) {
        binding.dialogTimePickerClose.setOnClickListener(action)
    }

    fun timeRange(start: Time? = null, end: Time? = null, default: Time? = null) {
        val s = start?.let {
            startTime = it
            TimeEntity.target(it.hour, it.minute, it.second)
        } ?: TimeEntity.target(
            startTime.hour, startTime.minute, startTime.second
        )
        val e = end?.let {
            endTime = it
            TimeEntity.target(it.hour, it.minute, it.second)
        } ?: TimeEntity.target(
            endTime.hour, endTime.minute, endTime.second
        )
        val d = default?.let {
            defaultTime = it
            TimeEntity.target(it.hour, it.minute, it.second)
        } ?: TimeEntity.target(
            defaultTime.hour, defaultTime.minute, defaultTime.second
        )
        binding.dialogTimePickerPicker.setRange(s, e, d)
    }

    companion object {

        const val KEY_TIME_SELECTED = "time_picker_dialog_time_selected"
    }
}