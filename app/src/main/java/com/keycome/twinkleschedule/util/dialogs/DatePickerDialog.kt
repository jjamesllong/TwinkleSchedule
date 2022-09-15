package com.keycome.twinkleschedule.util.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.keycome.twinkleschedule.base.BaseDialogFragment
import com.keycome.twinkleschedule.databinding.DialogDatePickerBinding
import com.keycome.twinkleschedule.extension.viewbindings.acquire

class DatePickerDialog : BaseDialogFragment() {

    private var _binding: DialogDatePickerBinding? = null
    val binding = _binding.acquire()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = DialogDatePickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}