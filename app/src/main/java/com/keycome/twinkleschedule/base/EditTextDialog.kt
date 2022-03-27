package com.keycome.twinkleschedule.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.keycome.twinkleschedule.databinding.DialogEditTextBinding
import com.keycome.twinkleschedule.extension.acquire

abstract class EditTextDialog : BaseDialogFragment() {

    private var _binding: DialogEditTextBinding? = null
    val binding get() = _binding.acquire()

    var inputType = INPUT_TYPE_SHORT_TEXT

    var inputErrorPredicate: ((Editable?) -> Boolean)? = null

    var inputErrorAction: ((Editable?) -> Unit)? = null

    var inputErrorHint: String = ""

    var title = ""
        set(param) {
            field = param
            binding.editTextDialogTitle.text = param
        }

    var confirmHint = ""
        set(param) {
            field = param
            binding.editTextDialogConfirmButton.text = param
        }

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
        onConfigure()
        when (inputType) {
            INPUT_TYPE_NUMBER -> {
                binding.editTextDialogField.inputType = InputType.TYPE_NUMBER_FLAG_SIGNED
            }
            INPUT_TYPE_SHORT_TEXT -> {
                binding.editTextDialogField.inputType = InputType.TYPE_CLASS_TEXT
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract fun onConfigure()

    fun onConfirm(action: (View) -> Unit) {
        binding.editTextDialogConfirmButton.setOnClickListener(action)
    }

    fun onCancel(action: (View) -> Unit) {
        binding.editTextDialogCancel.setOnClickListener(action)
    }

    fun onTextChange(textWatcher: TextWatcher) {
        binding.editTextDialogField.addTextChangedListener(textWatcher)
    }

    inline fun afterTextChanged(crossinline action: (editable: Editable?) -> Unit) {
        binding.editTextDialogField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                inputErrorPredicate?.invoke(s)?.let { condition ->
                    if (condition) {
                        inputErrorAction?.invoke(s)
                    } else {
                        action(s)
                    }
                } ?: action(s)
            }
        })
    }

    fun considerInputError(predicate: (Editable?) -> Boolean) {
        inputErrorPredicate = predicate
    }

    fun onInputError(action: (Editable?) -> Unit) {
        inputErrorAction = action
    }

    fun hintInputError() {

    }

    companion object {

        const val Title = "Title"
        const val Confirm = "Confirm"

        const val INPUT_TYPE_NUMBER = 1
        const val INPUT_TYPE_SHORT_TEXT = 2

    }
}