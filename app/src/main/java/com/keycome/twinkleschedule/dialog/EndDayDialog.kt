package com.keycome.twinkleschedule.dialog

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import com.keycome.twinkleschedule.base.BaseDialogFragment
import com.keycome.twinkleschedule.base.BaseViewModel
import com.keycome.twinkleschedule.databinding.DialogEndDayBinding
import com.keycome.twinkleschedule.model.EditScheduleViewModel
import com.keycome.twinkleschedule.record.interval.Day

class EndDayDialog : BaseDialogFragment() {

    private var _binding: DialogEndDayBinding? = null
    val binding get() = _binding!!

    val viewModel by viewModels<EndDayDialogViewModel>()

    private val gradientDrawable by lazy {
        GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = resources.displayMetrics.density * 8
            setColor(Color.BLACK)
            alpha = 40
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = DialogEndDayBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dialogEndDayCancel.setOnClickListener { dismiss() }
        binding.dialogEndDayConfirm.setOnClickListener {
            viewModel.liveSelectedDay.value?.let { selectedDay ->
                val otherDay = viewModel.liveEndDay?.value
                if (selectedDay != otherDay) {
                    viewModel.liveEndDay?.value = selectedDay
                }
            }
            dismiss()
        }
        viewModel.livePreviousDay.observe(viewLifecycleOwner) { day ->
            setBackground(day, null)
        }
        viewModel.liveSelectedDay.observe(viewLifecycleOwner) { day ->
            setBackground(day, gradientDrawable)
        }
        binding.dialogEndDayFriday.setOnClickListener {
            if (viewModel.liveSelectedDay.value == Day.Friday) {
                return@setOnClickListener
            }
            viewModel.livePreviousDay.value = viewModel.liveSelectedDay.value
            viewModel.liveSelectedDay.value = Day.Friday
        }
        binding.dialogEndDaySaturday.setOnClickListener {
            if (viewModel.liveSelectedDay.value == Day.Saturday) {
                return@setOnClickListener
            }
            viewModel.livePreviousDay.value = viewModel.liveSelectedDay.value
            viewModel.liveSelectedDay.value = Day.Saturday
        }
        binding.dialogEndDaySunday.setOnClickListener {
            if (viewModel.liveSelectedDay.value == Day.Sunday) {
                return@setOnClickListener
            }
            viewModel.livePreviousDay.value = viewModel.liveSelectedDay.value
            viewModel.liveSelectedDay.value = Day.Sunday
        }
        if (viewModel.liveSelectedDay.value == null) {
            viewModel.liveSelectedDay.value = viewModel.liveEndDay?.value
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setBackground(day: Day, background: GradientDrawable?) {
        when (day) {
            Day.Friday -> {
                binding.dialogEndDayFriday.background = background
            }
            Day.Saturday -> {
                binding.dialogEndDaySaturday.background = background
            }
            Day.Sunday -> {
                binding.dialogEndDaySunday.background = background
            }
            else -> {}
        }
    }

    class EndDayDialogViewModel : BaseViewModel() {

        val liveEndDay by shareOnlyVariable<MutableLiveData<Day>>(EditScheduleViewModel.sharedEndDay)

        val liveSelectedDay = MutableLiveData<Day>()

        val livePreviousDay = MutableLiveData<Day>()

        override fun onRemove() {
            super.onRemove()
            release(EditScheduleViewModel.sharedEndDay)
        }
    }
}