package com.keycome.twinkleschedule.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.lifecycle.lifecycleScope
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.base.BaseDialogFragment
import com.keycome.twinkleschedule.databinding.DialogRoutineDurationBinding
import com.keycome.twinkleschedule.delivery.Pipette
import com.keycome.twinkleschedule.delivery.Pipette.distribute
import com.keycome.twinkleschedule.util.const.KEY_ROUTINE_SECTION_DURATION
import kotlinx.coroutines.launch

class RoutineDurationDialog : BaseDialogFragment() {

    private var _binding: DialogRoutineDurationBinding? = null
    val binding get() = _binding!!

    private var duration = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = DialogRoutineDurationBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        duration = savedInstanceState?.getInt(
            KEY_ROUTINE_SECTION_DURATION
        ) ?: arguments?.getInt(
            KEY_ROUTINE_SECTION_DURATION
        ) ?: 0

        binding.dialogRoutineDurationProgress.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    duration = progress
                    binding.dialogRoutineDurationText.text = getString(
                        R.string.dialog_routine_duration_progress,
                        progress
                    )
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }

            }
        )
        binding.dialogRoutineDurationProgress.progress = duration
        binding.dialogRoutineDurationCancel.setOnClickListener { dismiss() }
        binding.dialogRoutineDurationConfirm.setOnClickListener {
            lifecycleScope.launch {
                lifecycleScope.launch {
                    Pipette.forInt.distribute(KEY_ROUTINE_SECTION_DURATION) { duration }
                    dismiss()
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_ROUTINE_SECTION_DURATION, duration)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}