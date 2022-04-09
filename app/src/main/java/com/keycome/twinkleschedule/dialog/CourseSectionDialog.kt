package com.keycome.twinkleschedule.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.keycome.twinkleschedule.adapter.SectionWheelAdapter
import com.keycome.twinkleschedule.base.BaseDialogFragment
import com.keycome.twinkleschedule.databinding.DialogCourseSectionBinding
import com.keycome.twinkleschedule.delivery.Drop
import com.keycome.twinkleschedule.delivery.Pipette
import com.keycome.twinkleschedule.extension.acquire
import kotlinx.coroutines.launch

class CourseSectionDialog : BaseDialogFragment() {

    private var _binding: DialogCourseSectionBinding? = null
    val binding get() = _binding.acquire()

    private val startSectionInItem by lazy { arguments?.getInt(StartSectionInItem) ?: 0 }
    private val endSectionInItem by lazy { arguments?.getInt(EndSectionInItem) ?: 0 }

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
        if (savedInstanceState == null) {
            if (startSectionInItem != 0) {
                binding.startSectionWheel.currentItem = startSectionInItem - 1
            }
            if (endSectionInItem != 0) {
                binding.endSectionWheel.currentItem = endSectionInItem - 1
            }

        } else {
            val savedStartIndex = savedInstanceState.getInt(StartSection)
            val savedEndIndex = savedInstanceState.getInt(EndSection)
            if (savedStartIndex != 0) {
                binding.startSectionWheel.currentItem = savedStartIndex
            }
            if (savedEndIndex != 0) {
                binding.endSectionWheel.currentItem = savedEndIndex
            }
        }
        binding.dialogCourseSectionCancel.setOnClickListener { dismiss() }
        val startAdapter = SectionWheelAdapter()
        val endAdapter = SectionWheelAdapter()
        binding.startSectionWheel.apply {
            setCyclic(true)
            adapter = startAdapter
            setOnItemSelectedListener {
                if (binding.endSectionWheel.currentItem < it) {
                    binding.endSectionWheel.currentItem = it
                }
            }
        }
        binding.endSectionWheel.apply {
            setCyclic(true)
            adapter = endAdapter
            setOnItemSelectedListener {
                if (binding.startSectionWheel.currentItem > it) {
                    binding.startSectionWheel.currentItem = it
                }
            }
        }
        binding.dialogCourseSectionConfirm.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val startSection = startAdapter.getItem(binding.startSectionWheel.currentItem)
                val endSection = endAdapter.getItem(binding.endSectionWheel.currentItem)
                if (endSection >= startSection) {
                    Pipette.pipetteForInt.emit(Drop(StartSection, startSection))
                    Pipette.pipetteForInt.emit(Drop(EndSection, endSection))
                }
                dismiss()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(StartSection, binding.startSectionWheel.currentItem)
        outState.putInt(EndSection, binding.endSectionWheel.currentItem)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val StartSection = "course_start_section"
        const val EndSection = "course_end_section"
        const val StartSectionInItem = "course_start_section_in_item"
        const val EndSectionInItem = "course_end_section_in_item"
    }

}