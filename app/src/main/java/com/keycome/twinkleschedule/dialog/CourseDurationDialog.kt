package com.keycome.twinkleschedule.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.SeekBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.keycome.twinkleschedule.base.BaseDialogFragment
import com.keycome.twinkleschedule.base.BaseViewModel2
import com.keycome.twinkleschedule.databinding.DialogCourseDurationBinding
import com.keycome.twinkleschedule.model.EditScheduleViewModel

class CourseDurationDialog : BaseDialogFragment() {

    private var _binding: DialogCourseDurationBinding? = null
    val binding get() = _binding!!

    val viewModel by viewModels<CourseDurationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = DialogCourseDurationBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dialogCourseDurationSeekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    viewModel.liveSelectedDuration.value = progress
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }

            })
        binding.dialogCourseDurationCancel.setOnClickListener { dismiss() }
        binding.dialogCourseDurationConfirm.setOnClickListener {
            viewModel.liveCourseDuration?.value = binding.dialogCourseDurationSeekBar.progress
            dismiss()
        }
        viewModel.liveSelectedDuration.observe(viewLifecycleOwner) { duration ->
            binding.dialogCourseDurationProgressText.text = duration.toString() + " 分钟"
        }
        viewModel.liveCourseDuration?.value?.let { duration ->
            if (viewModel.liveSelectedDuration.value == null) {
                binding.dialogCourseDurationSeekBar.progress = duration
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class CourseDurationViewModel : BaseViewModel2() {

        val liveCourseDuration by shareOnlyVariable<MutableLiveData<Int>>(
            EditScheduleViewModel.sharedCourseDuration
        )

        val liveSelectedDuration = MutableLiveData<Int>()

        override fun onCleared() {
            super.onCleared()
            release(EditScheduleViewModel.sharedCourseDuration)
        }
    }
}