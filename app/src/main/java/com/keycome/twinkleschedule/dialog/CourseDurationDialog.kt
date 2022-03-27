package com.keycome.twinkleschedule.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.keycome.twinkleschedule.base.BaseDialogFragment
import com.keycome.twinkleschedule.base.BaseViewModel
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
        super.onCreateView(inflater, container, savedInstanceState)
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

    class CourseDurationViewModel : BaseViewModel() {

        val liveCourseDuration by shareOnlyVariable<MutableLiveData<Int>>(
            EditScheduleViewModel.sharedCourseDuration
        )

        val liveSelectedDuration = MutableLiveData<Int>()

        override fun onRemove() {
            super.onRemove()
            release(EditScheduleViewModel.sharedCourseDuration)
        }
    }
}