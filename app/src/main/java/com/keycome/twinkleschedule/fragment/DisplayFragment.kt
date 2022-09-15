package com.keycome.twinkleschedule.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.base.BaseFragment
import com.keycome.twinkleschedule.databinding.FragmentDisplayBinding
import com.keycome.twinkleschedule.viewmodel.DisplayViewModel
import com.keycome.twinkleschedule.util.const.KEY_COURSE
import com.keycome.twinkleschedule.widget.timetable.PagingAdapter

class DisplayFragment : BaseFragment() {

    val viewModel by viewModels<DisplayViewModel>()

    private var _binding: FragmentDisplayBinding? = null
    val binding get() = _binding!!

    private val navController: NavController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentDisplayBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = PagingAdapter {
            viewModel.getDisplayCourseById(it)?.also { course ->
                navController.navigate(
                    R.id.action_displayFragment_to_courseDetailsDialog,
                    Bundle().apply { putParcelable(KEY_COURSE, course) }
                )
            }
        }
        binding.tableViewPager.adapter = adapter
        binding.tableViewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    viewModel.setWeekSelected(position + 1)
                }
            }
        )
        viewModel.liveDisplayStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                DisplayViewModel.NOTHING ->
                    binding.fragmentDisplayCoursesToolbar.title = getString(
                        R.string.fragment_display_noting
                    )
                DisplayViewModel.UPCOMING ->
                    binding.fragmentDisplayCoursesToolbar.subtitle = getString(
                        R.string.fragment_display_upcoming
                    )
                DisplayViewModel.ENDED ->
                    binding.fragmentDisplayCoursesToolbar.subtitle = getString(
                        R.string.fragment_display_ended
                    )
            }
        }
        viewModel.liveWeekSelected.observe(viewLifecycleOwner) { week ->
            if (week != 0) {
                binding.fragmentDisplayCoursesToolbar.title = getString(
                    R.string.fragment_display_week, week
                )
                val status = viewModel.getDisplayStatus()
                if (status > 0) {
                    binding.fragmentDisplayCoursesToolbar.subtitle = when {
                        week < status -> getString(
                            R.string.fragment_display_ended
                        )
                        week > status -> getString(
                            R.string.fragment_display_upcoming
                        )
                        else -> null
                    }
                }
            }
        }
        viewModel.liveDescriber.observe(viewLifecycleOwner) {
            adapter.submitDescriber(it)
            val status = viewModel.getDisplayStatus()
            if (status > 0) {
                binding.tableViewPager.setCurrentItem(status - 1, false)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}