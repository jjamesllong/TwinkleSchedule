package com.keycome.twinkleschedule.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.github.gzuliyujiang.wheelview.contract.OnWheelChangedListener
import com.github.gzuliyujiang.wheelview.widget.WheelView
import com.keycome.twinkleschedule.base.BaseDialogFragment
import com.keycome.twinkleschedule.databinding.DialogCourseSectionBinding
import com.keycome.twinkleschedule.delivery.Pipette
import com.keycome.twinkleschedule.delivery.Pipette.distribute
import com.keycome.twinkleschedule.extension.acquire
import com.keycome.twinkleschedule.extension.ints.storeWith
import com.keycome.twinkleschedule.util.const.KEY_SCHEDULE_END_SECTION
import kotlinx.coroutines.launch

class CourseSectionDialog : BaseDialogFragment() {

    private var _binding: DialogCourseSectionBinding? = null
    val binding get() = _binding.acquire()

    private var courseSection = 0

    private var startSection = 0

    private var endSection = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = DialogCourseSectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val courseSection = savedInstanceState?.getInt(
            KEY_SCHEDULE_END_SECTION
        ) ?: arguments?.getInt(
            KEY_SCHEDULE_END_SECTION
        ) ?: 0
        startSection = savedInstanceState?.getInt(
            KEY_START_SECTION
        ) ?: arguments?.getInt(
            KEY_START_SECTION
        ) ?: 0
        endSection = savedInstanceState?.getInt(
            KEY_END_SECTION
        ) ?: arguments?.getInt(
            KEY_END_SECTION
        ) ?: 0
        val range = if (courseSection == 0) emptyList() else (1..courseSection).toList()
        binding.dialogCourseSectionStartSectionWheel.data = range
        binding.dialogCourseNameEndSectionWheel.data = range
        if (range.isEmpty()) {
            startSection = 0
            endSection = 0
        } else {
            if (startSection == 0) {
                startSection = 1
            }
            if (endSection == 0) {
                endSection = 1
            }
            binding.dialogCourseSectionStartSectionWheel.scrollTo(startSection - 1)
            binding.dialogCourseNameEndSectionWheel.scrollTo(endSection - 1)
        }
        binding.dialogCourseSectionStartSectionWheel.setOnWheelChangedListener(
            object : OnWheelChangedListener {

                override fun onWheelScrolled(view: WheelView?, offset: Int) {
                }

                override fun onWheelSelected(view: WheelView?, position: Int) {
                    startSection = position + 1
                    if (startSection > endSection) {
                        binding.dialogCourseNameEndSectionWheel.smoothScrollTo(position)
                        endSection = startSection
                    }
                }

                override fun onWheelScrollStateChanged(view: WheelView?, state: Int) {
                }

                override fun onWheelLoopFinished(view: WheelView?) {
                }

            }
        )
        binding.dialogCourseNameEndSectionWheel.setOnWheelChangedListener(
            object : OnWheelChangedListener {
                override fun onWheelScrolled(view: WheelView?, offset: Int) {
                }

                override fun onWheelSelected(view: WheelView?, position: Int) {
                    endSection = position + 1
                    if (endSection < startSection) {
                        binding.dialogCourseSectionStartSectionWheel.smoothScrollTo(position)
                        startSection = endSection
                    }
                }

                override fun onWheelScrollStateChanged(view: WheelView?, state: Int) {
                }

                override fun onWheelLoopFinished(view: WheelView?) {
                }

            }
        )
        binding.dialogCourseSectionCancel.setOnClickListener { dismiss() }
        binding.dialogCourseSectionConfirm.setOnClickListener {
            val x = startSection storeWith endSection
            lifecycleScope.launch {
                Pipette.forInt.distribute(KEY_SECTION_PAIR) { x }
                dismiss()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.apply {
            putInt(KEY_SCHEDULE_END_SECTION, courseSection)
            putInt(KEY_START_SECTION, startSection)
            putInt(KEY_END_SECTION, endSection)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val KEY_SECTION_PAIR = "section_pair"
        const val KEY_START_SECTION = "start_section"
        const val KEY_END_SECTION = "end_section"
    }

}