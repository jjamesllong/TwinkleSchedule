package com.keycome.twinkleschedule.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.base.BaseFragment
import com.keycome.twinkleschedule.custom.courseschedule.PagerAdapter
import com.keycome.twinkleschedule.databinding.FragmentDisplayCoursesBinding
import com.keycome.twinkleschedule.model.DisplayCoursesViewModel

class DisplayCoursesFragment : BaseFragment() {

    val viewModel by viewModels<DisplayCoursesViewModel>()

    private var _binding: FragmentDisplayCoursesBinding? = null
    val binding get() = _binding!!

    private val navController: NavController by lazy { findNavController() }

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
        val pagerAdapter = PagerAdapter()
        binding.tableViewPager2.adapter = pagerAdapter
        binding.tableViewPager2.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    viewModel.refreshWeekSelected(position + 1)
                }
            }
        )
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
            binding.fragmentDisplayCoursesToolbar.title = StringBuilder()
                .append("第 ")
                .append(it)
                .append(" 周")
                .toString()
            if (it != viewModel.liveWeekNow.value) {
                binding.fragmentDisplayCoursesToolbar.subtitle = "非本周"
            } else {
                binding.fragmentDisplayCoursesToolbar.subtitle = null
            }
            viewModel.liveParentSchedule.value?.let { schedule ->
                viewModel.refreshCourseScheduleList(schedule)
            }
        }
        viewModel.liveCourseScheduleList.observe(viewLifecycleOwner) {
            pagerAdapter.submitList(it)
            viewModel.liveWeekSelected.value?.let { week ->
                binding.tableViewPager2.setCurrentItem(week - 1, false)
            }
        }
        pagerAdapter.registerCourseViewClickListener {
            viewModel.refreshDialogCourse(it.courseId)
            navController.navigate(R.id.action_displayCoursesFragment_to_courseDetailsDialog)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}