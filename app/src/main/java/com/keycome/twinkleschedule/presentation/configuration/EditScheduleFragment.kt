package com.keycome.twinkleschedule.presentation.configuration

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.keycome.twinkleschedule.BaseFragment
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.custom.DatePickerDialog
import com.keycome.twinkleschedule.custom.EditTextDialog
import com.keycome.twinkleschedule.custom.TimePickerDialog
import com.keycome.twinkleschedule.custom.WheelDialog
import com.keycome.twinkleschedule.databinding.CustomToolbarLayoutBinding
import com.keycome.twinkleschedule.databinding.FragmentEditScheduleBinding
import com.keycome.twinkleschedule.extension.toast

class EditScheduleFragment : BaseFragment<FragmentEditScheduleBinding>(), View.OnClickListener {

    private lateinit var safeContext: Context

    private val editTextDialog: EditTextDialog by lazy {
        EditTextDialog(safeContext) {
            onTextChanged {
                if (it == "hello") toast(it)
            }
        }
    }

    private val datePickerDialog: DatePickerDialog by lazy {
        DatePickerDialog(safeContext) {
            datePickerPosition = "2021-06-04"
        }
    }

    private val timePickerDialog: TimePickerDialog by lazy {
        TimePickerDialog(safeContext) {
            timePickerPosition = arrayOf(17, 12)
        }
    }

    private val wheelDialog: WheelDialog by lazy {
        WheelDialog(safeContext)
    }

    override fun deployBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentEditScheduleBinding {
        return FragmentEditScheduleBinding.inflate(inflater, container, false)
    }

    override fun supportToolbar(title: Array<Int>): CustomToolbarLayoutBinding {
        title[0] = R.string.editScheduleFragmentLabel
        return binding.toolbar
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        safeContext = requireContext()
        for (child in binding.linearLayout.children)
            child.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.editScheduleNameItem.id -> editTextDialog.show()
            binding.editSchoolBeginDateItem.id -> datePickerDialog.show()
            binding.editCourseNumberItem.id -> wheelDialog.show()
            else -> toast("none")
        }
    }
}