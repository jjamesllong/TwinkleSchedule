package com.keycome.twinkleschedule.presentation.record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.keycome.twinkleschedule.BaseFragment
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.databinding.FragmentEditCourseBinding
import com.keycome.twinkleschedule.databinding.ViewToolbarLayoutBinding
import com.keycome.twinkleschedule.record.sketch.CourseField

class EditCourseFragment : BaseFragment<FragmentEditCourseBinding>() {

    private val viewModel by activityViewModels<RecordViewModel>()
    private val args by navArgs<EditCourseFragmentArgs>()

    override fun supportBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentEditCourseBinding {
        return FragmentEditCourseBinding.inflate(inflater, container, false)
    }

    override fun supportToolbar(title: Array<Int>): ViewToolbarLayoutBinding {
        title[0] = R.string.addCourseFragmentLabel
        return binding.fragmentAddCourseToolbar
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.initEditingCourseList(CourseField.ParentScheduleId(args.scheduleId))
        val addCourseAdapter = EditCourseAdapter(viewModel)
        val addCourseHeaderAdapter = EditCourseHeaderAdapter(viewModel)
        val concatAdapter = ConcatAdapter(addCourseHeaderAdapter, addCourseAdapter)
        binding.editingCourseRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = concatAdapter
        }
        viewModel.liveEditingCourse.observe(viewLifecycleOwner) {
            addCourseHeaderAdapter.updateList(it)
            addCourseAdapter.submitList(it)
        }
        binding.editingCourseAddButton.setOnClickListener {
            val resultSize = viewModel.liveEditingCourse.addCourse()
            binding.editingCourseRecyclerView.smoothScrollToPosition(resultSize)
        }
        binding.editingCourseSaveButton.setOnClickListener {
            viewModel.insertEditingCourse()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clearEditingCourseList()
    }

}