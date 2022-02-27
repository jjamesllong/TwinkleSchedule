package com.keycome.twinkleschedule.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.keycome.twinkleschedule.base.BaseFragment
import com.keycome.twinkleschedule.custom.courseschedule.toCourseTable
import com.keycome.twinkleschedule.custom.courseschedule.toDayTable
import com.keycome.twinkleschedule.custom.courseschedule.toSectionTable
import com.keycome.twinkleschedule.databinding.FragmentDisplayCoursesBinding
import com.keycome.twinkleschedule.model.DisplayCoursesViewModel

class DisplayCoursesFragment : BaseFragment() {

    val viewModel by viewModels<DisplayCoursesViewModel>()

    private var _binding: FragmentDisplayCoursesBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentDisplayCoursesBinding.inflate(
            inflater,
            container,
            attachToParentFalseToo
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dayTable = binding.dayRecyclerView.toDayTable()
        val sectionTable = binding.sectionRecyclerView.toSectionTable()
        val courseTable = binding.courseRecyclerView.toCourseTable()
        viewModel.liveDisplayScheduleId.observe(viewLifecycleOwner) {
            viewModel.refreshParentSchedule(it)
        }
        viewModel.liveParentSchedule.observe(viewLifecycleOwner) {
            viewModel.refreshWeekNow(it)
        }
        viewModel.liveWeekNow.observe(viewLifecycleOwner) {
            viewModel.refreshWeekSelected(it)
        }
        viewModel.liveWeekSelected.observe(viewLifecycleOwner) {
            viewModel.refreshCourseList(it)
            binding.fragmentDisplayCoursesToolbar.title = StringBuilder()
                .append("第 ")
                .append(it)
                .append(" 周")
                .toString()
        }
        viewModel.liveCourseList.observe(viewLifecycleOwner) {
            viewModel.refreshCourseSchedule()
        }
        viewModel.liveCourseSchedule.observe(viewLifecycleOwner) {
            dayTable.submitData(it.schedule)
            sectionTable.submitData(it.schedule)
            courseTable.submitData(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}