package com.keycome.twinkleschedule.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.base.BaseDialogFragment
import com.keycome.twinkleschedule.base.BaseViewModel
import com.keycome.twinkleschedule.databinding.DialogCourseDurationBinding
import com.keycome.twinkleschedule.delivery.Drop
import com.keycome.twinkleschedule.delivery.Pipette
import com.keycome.twinkleschedule.model.EditDailyRoutineViewModel
import kotlinx.coroutines.launch

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
                    viewModel.setCourseDuration(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }

            })
        binding.dialogCourseDurationCancel.setOnClickListener { dismiss() }
        binding.dialogCourseDurationConfirm.setOnClickListener {
            lifecycleScope.launch {
                val duration = viewModel.getCourseDuration()
                lifecycleScope.launch {
                    Pipette.forInt.emit(
                        Drop(EditDailyRoutineViewModel.courseDuration, duration)
                    )
                    dismiss()
                }
            }
        }
        viewModel.liveCourseDuration.observe(viewLifecycleOwner) { duration ->
            binding.dialogCourseDurationProgressText.text =
                getString(R.string.dialog_course_duration_progress, duration)
        }
        binding.dialogCourseDurationSeekBar.progress = viewModel.getCourseDuration()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class CourseDurationViewModel : BaseViewModel() {

        private val _liveCourseDuration = MutableLiveData(45)
        val liveCourseDuration: LiveData<Int> get() = _liveCourseDuration

        fun setCourseDuration(duration: Int) {
            _liveCourseDuration.value = duration
        }

        fun getCourseDuration() = _liveCourseDuration.value ?: 0
    }
}