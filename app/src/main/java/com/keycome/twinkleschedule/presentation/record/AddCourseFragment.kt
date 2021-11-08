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
import com.keycome.twinkleschedule.databinding.FragmentAddCourseBinding
import com.keycome.twinkleschedule.databinding.ViewToolbarLayoutBinding
import com.keycome.twinkleschedule.model.sketch.CourseField

class AddCourseFragment : BaseFragment<FragmentAddCourseBinding>() {

    private val viewModel by activityViewModels<RecordViewModel>()
    private val args by navArgs<AddCourseFragmentArgs>()

    override fun supportBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddCourseBinding {
        return FragmentAddCourseBinding.inflate(inflater, container, false)
    }

    override fun supportToolbar(title: Array<Int>): ViewToolbarLayoutBinding {
        title[0] = R.string.addCourseFragmentLabel
        return binding.fragmentAddCourseToolbar
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.initLiveCourseList(CourseField.ParentScheduleId(args.scheduleId))
        val addCourseAdapter = AddCourseAdapter(viewModel)
        val addCourseHeaderAdapter = AddCourseHeaderAdapter(viewModel)
        val concatAdapter = ConcatAdapter(addCourseHeaderAdapter, addCourseAdapter)
        binding.editingCourseRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = concatAdapter
        }
        viewModel.liveCourseList.observe(viewLifecycleOwner) {
            addCourseHeaderAdapter.updateList(it)
            addCourseAdapter.submitList(it)
        }
        binding.editingCourseAddButton.setOnClickListener {
            val resultSize = viewModel.liveCourseList.addCourse()
            binding.editingCourseRecyclerView.smoothScrollToPosition(resultSize)
        }
        binding.editingCourseSaveButton.setOnClickListener {
            viewModel.insertCourse()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clearLiveCourseList()
    }

}