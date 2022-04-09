package com.keycome.twinkleschedule.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.keycome.twinkleschedule.base.BaseDialogFragment
import com.keycome.twinkleschedule.databinding.DialogCourseWeekBinding
import com.keycome.twinkleschedule.delivery.Drop
import com.keycome.twinkleschedule.delivery.Pipette
import com.keycome.twinkleschedule.extension.acquire
import com.keycome.twinkleschedule.util.SlidingTriggerListener
import kotlinx.coroutines.launch

class CourseWeekDialog : BaseDialogFragment() {

    private var _binding: DialogCourseWeekBinding? = null
    val binding get() = _binding.acquire()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = DialogCourseWeekBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val childrenList = listOf(
            binding.week1, binding.week2, binding.week3, binding.week4,
            binding.week5, binding.week6, binding.week7, binding.week8,
            binding.week9, binding.week10, binding.week11, binding.week12,
            binding.week13, binding.week14, binding.week15, binding.week16,
            binding.week17, binding.week18, binding.week19, binding.week20,
            binding.week21, binding.week22, binding.week23, binding.week24
        )
        binding.dialogCourseWeekCancel.setOnClickListener { dismiss() }
        binding.dialogCourseWeekAll.setOnClickListener {
            if (it.isSelected) {
                childrenList.forEach { v -> v.isSelected = false }
                binding.dialogCourseWeekAll.text = "全选"
            } else {
                childrenList.forEach { v -> v.isSelected = true }
                binding.dialogCourseWeekAll.text = "全不选"
            }
            it.isSelected = !it.isSelected
            binding.dialogCourseWeekEven.isSelected = false
            binding.dialogCourseWeekOdd.isSelected = false
        }
        binding.dialogCourseWeekEven.setOnClickListener {
            if (it.isSelected) {
                childrenList.forEach { v -> v.isSelected = false }
            } else {
                childrenList.forEachIndexed { index, textView ->
                    textView.isSelected = index % 2 != 0
                }
            }
            it.isSelected = !it.isSelected
            binding.dialogCourseWeekAll.isSelected = false
            binding.dialogCourseWeekAll.text = "全选"
            binding.dialogCourseWeekOdd.isSelected = false
        }
        binding.dialogCourseWeekOdd.setOnClickListener {
            if (it.isSelected) {
                childrenList.forEach { v -> v.isSelected = false }
            } else {
                childrenList.forEachIndexed { index, textView ->
                    textView.isSelected = index % 2 == 0
                }
            }
            it.isSelected = !it.isSelected
            binding.dialogCourseWeekAll.isSelected = false
            binding.dialogCourseWeekAll.text = "全选"
            binding.dialogCourseWeekEven.isSelected = false
        }
        binding.dialogCourseWeekContainer.setOnTouchListener(
            SlidingTriggerListener(childrenList) { code ->
                when (code) {
                    0 -> {
                        binding.dialogCourseWeekAll.isSelected = false
                        binding.dialogCourseWeekAll.text = "全选"
                        binding.dialogCourseWeekEven.isSelected = false
                        binding.dialogCourseWeekOdd.isSelected = false
                    }
                    1 -> {
                        binding.dialogCourseWeekAll.isSelected = true
                        binding.dialogCourseWeekAll.text = "全不选"
                        binding.dialogCourseWeekEven.isSelected = false
                        binding.dialogCourseWeekOdd.isSelected = false
                    }
                    2 -> {
                        binding.dialogCourseWeekAll.isSelected = false
                        binding.dialogCourseWeekAll.text = "全选"
                        binding.dialogCourseWeekEven.isSelected = true
                        binding.dialogCourseWeekOdd.isSelected = false
                    }
                    3 -> {
                        binding.dialogCourseWeekAll.isSelected = false
                        binding.dialogCourseWeekAll.text = "全选"
                        binding.dialogCourseWeekEven.isSelected = false
                        binding.dialogCourseWeekOdd.isSelected = true
                    }
                    4 -> {
                        binding.dialogCourseWeekAll.isSelected = false
                        binding.dialogCourseWeekAll.text = "全选"
                        binding.dialogCourseWeekEven.isSelected = false
                        binding.dialogCourseWeekOdd.isSelected = false
                    }
                }
            }
        )
        binding.dialogCourseWeekConfirm.setOnClickListener {
            lifecycleScope.launch {
                Pipette.pipetteForInt.emit(Drop(Start_EMITING_TAG, 0))
                childrenList.forEachIndexed { index, textView ->
                    if (textView.isSelected) {
                        Pipette.pipetteForInt.emit(Drop(COURSE_WEEK_TAG, index + 1))
                    }
                }
                Pipette.pipetteForInt.emit(Drop(END_EMITTING_TAG, 0))
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val Start_EMITING_TAG = "start_emitting_tag"
        const val COURSE_WEEK_TAG = "course_week_tag"
        const val END_EMITTING_TAG = "end_emitting_tag"
    }
}