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
import com.keycome.twinkleschedule.databinding.DialogDailyCoursesBinding
import com.keycome.twinkleschedule.model.EditScheduleViewModel

class DailyCoursesDialog : BaseDialogFragment() {

    val viewModel by viewModels<DailyCoursesViewModel>()

    private var _binding: DialogDailyCoursesBinding? = null
    val binding get() = _binding!!

    private var previousIndex = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = DialogDailyCoursesBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val itemList = listOf(
            binding.coursesOption1,
            binding.coursesOption2,
            binding.coursesOption3,
            binding.coursesOption4,
            binding.coursesOption5,
            binding.coursesOption6,
            binding.coursesOption7,
            binding.coursesOption8,
            binding.coursesOption9
        )
        val background = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = resources.displayMetrics.density * 8
            setColor(Color.BLACK)
            alpha = 40
        }
        viewModel.liveSelectedIndex.observe(viewLifecycleOwner) { index ->
            val currentNumber = viewModel.liveDailyCourses?.value ?: 0
            if (index == -1) {
                if (currentNumber != 0) {
                    val currentIndex = currentNumber - diff
                    itemList[currentIndex].background = background
                    previousIndex = currentIndex
                }
            } else {
                itemList[index].background = background
                if (previousIndex != -1) {
                    itemList[previousIndex].background = null
                }
                previousIndex = index
            }
        }
        for (i in itemList.indices) {
            itemList[i].setOnClickListener {
                if (i == previousIndex) return@setOnClickListener
                viewModel.liveSelectedIndex.value = i
            }
        }
        binding.dialogDailyCoursesConfirm.setOnClickListener {
            val selectedIndex = viewModel.liveSelectedIndex.value ?: -1
            if (selectedIndex != -1) {
                viewModel.liveDailyCourses?.value = selectedIndex + diff
            }
            dismiss()
        }
        binding.dialogDailyCoursesCancel.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class DailyCoursesViewModel : BaseViewModel() {

        val liveDailyCourses by shareOnlyVariable<MutableLiveData<Int>>(
            EditScheduleViewModel.sharedDailyCourses
        )

        val liveSelectedIndex = MutableLiveData(-1)

        override fun onRemove() {
            super.onRemove()
            release(EditScheduleViewModel.sharedDailyCourses)
        }
    }

    companion object {
        const val diff = 6
    }
}