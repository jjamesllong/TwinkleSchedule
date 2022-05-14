package com.keycome.twinkleschedule.base

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.keycome.twinkleschedule.databinding.DialogEditTextBinding
import com.keycome.twinkleschedule.extension.acquire
import com.keycome.twinkleschedule.util.TextWatcherScope
import com.keycome.twinkleschedule.util.TextWatcherScopeImpl

abstract class EditTextDialog : BaseDialogFragment() {

    private var _binding: DialogEditTextBinding? = null
    val binding get() = _binding.acquire()

    var editText: Editable
        get() = binding.editTextDialogField.text
        set(param) {
            binding.editTextDialogField.text = param
        }

    var inputType = INPUT_TYPE_SHORT_TEXT
        set(param) {
            field = param
            when (param) {
                INPUT_TYPE_SHORT_TEXT ->
                    binding.editTextDialogField.inputType = InputType.TYPE_CLASS_TEXT
                INPUT_TYPE_NUMBER ->
                    binding.editTextDialogField.inputType = InputType.TYPE_CLASS_NUMBER
            }
        }

    var title = "title"
        set(param) {
            field = param
            binding.editTextDialogTitle.text = param
        }

    var confirm = "OK"
        set(param) {
            field = param
            binding.editTextDialogConfirm.text = param
        }

    abstract fun configure()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = DialogEditTextBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configure()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun onConfirm(action: (View) -> Unit) {
        binding.editTextDialogConfirm.setOnClickListener(action)
    }

    fun onCancel(action: (View) -> Unit) {
        binding.editTextDialogCancel.setOnClickListener(action)
    }

    fun textWatcher(action: TextWatcherScope.() -> Unit) {
        val scopeImpl = TextWatcherScopeImpl(binding.editTextDialogField)
        scopeImpl.action()
    }

    companion object {

        const val TAG = "EditTextDialog"

        const val INPUT_TYPE_NUMBER = 1
        const val INPUT_TYPE_SHORT_TEXT = 2

    }
}