package com.keycome.twinkleschedule.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.base.BaseDialogFragment
import com.keycome.twinkleschedule.databinding.DialogTextColorBinding
import com.keycome.twinkleschedule.delivery.Pipette
import com.keycome.twinkleschedule.delivery.Pipette.distribute
import com.keycome.twinkleschedule.extension.viewbindings.acquire
import com.keycome.twinkleschedule.util.const.KEY_COURSE_TEXT_COLOR
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val defaultLightColor = (0xFFFFFFFF).toInt()
const val defaultDarkColor = (0xFF000000).toInt()

class TextColorDialog : BaseDialogFragment() {

    private var _binding: DialogTextColorBinding? = null
    private val binding get() = _binding.acquire()

    private var selectedTextColor = defaultDarkColor

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = DialogTextColorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedTextColor = savedInstanceState?.getInt(
            KEY_COURSE_TEXT_COLOR
        ) ?: arguments?.getInt(
            KEY_COURSE_TEXT_COLOR
        ) ?: 0

        when (selectedTextColor) {
            defaultLightColor -> binding.dialogTextColorRadioGroup.check(
                R.id.dialog_text_color_light
            )
            defaultDarkColor -> binding.dialogTextColorRadioGroup.check(
                R.id.dialog_text_color_dark
            )
        }

        binding.dialogTextColorClose.setOnClickListener {
            dismiss()
        }
        binding.dialogTextColorRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            val color = when (checkedId) {
                R.id.dialog_text_color_light -> defaultLightColor
                else -> defaultDarkColor
            }
            viewLifecycleOwner.lifecycleScope.launch {
                Pipette.forInt.distribute(KEY_COURSE_TEXT_COLOR) { color }
                delay(400)
                dismiss()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_COURSE_TEXT_COLOR, selectedTextColor)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}