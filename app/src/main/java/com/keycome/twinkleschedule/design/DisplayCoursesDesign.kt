package com.keycome.twinkleschedule.design

import android.util.Log
import com.keycome.twinkleschedule.base.BindingDesign
import com.keycome.twinkleschedule.custom.courseschedule.toCourseTable
import com.keycome.twinkleschedule.custom.courseschedule.toDayTable
import com.keycome.twinkleschedule.custom.courseschedule.toSectionTable
import com.keycome.twinkleschedule.databinding.FragmentDisplayCoursesBinding
import com.keycome.twinkleschedule.fragment.DisplayCoursesFragment
import com.keycome.twinkleschedule.pipette.DisplayCoursesPipette

class DisplayCoursesDesign(
    private val fragment: DisplayCoursesFragment
) : BindingDesign<FragmentDisplayCoursesBinding>() {

    val pipette: DisplayCoursesPipette by pipettes()


    override fun onBind(binding: FragmentDisplayCoursesBinding) {
        Log.d("DisplayCoursesDesign", "onBind()")
        fragment.viewModel.liveCourseSchedule.observe(fragment.viewLifecycleOwner) {
            binding.dayRecyclerView.toDayTable(it.schedule)
            binding.sectionRecyclerView.toSectionTable(it.schedule)
            binding.courseRecyclerView.toCourseTable(
                fragment.viewLifecycleOwner,
                it.schedule,
                it.courseList
            ) {

            }
        }
        fragment.viewModel.liveWeekNow.observe(fragment.viewLifecycleOwner) {
            binding.fragmentDisplayCoursesToolbar.title = StringBuilder()
                .append("第 ")
                .append(it)
                .append(" 周")
        }
    }

    override fun onInit() {
    }

    override suspend fun onAsync() {
    }

}