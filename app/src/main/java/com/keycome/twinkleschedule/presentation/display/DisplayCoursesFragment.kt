package com.keycome.twinkleschedule.presentation.display

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.keycome.twinkleschedule.database.Day
import com.keycome.twinkleschedule.database.TestData
import com.keycome.twinkleschedule.databinding.FragmentDisplayCoursesBinding

class DisplayCoursesFragment : Fragment() {
    private lateinit var binding: FragmentDisplayCoursesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDisplayCoursesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val courseArray = TestData.courseArray
        binding.gridLayout.apply { rowCount = TestData.courseSchedule.courses; columnCount = 5 }
        lifecycleScope.launchWhenCreated {
            for (course in courseArray) {
                val params = GridLayout.LayoutParams().apply { width = 0; height = 0 }
                val text = TextView(requireContext())
                params.rowSpec = GridLayout.spec(
                    course.section.startSection - 1,
                    course.section.endSection - course.section.startSection + 1,
                    1f
                )
                params.columnSpec = GridLayout.spec(
                    convertDayToColumn(course.day), 1, 1f
                )
                text.text = course.title
                binding.gridLayout.addView(text, params)
            }
        }
    }

    private fun convertDayToColumn(day: Day) = when (day) {
        Day.Monday -> 0
        Day.Tuesday -> 1
        Day.Wednesday -> 2
        Day.Thursday -> 3
        Day.Friday -> 4
        else -> -1
    }
}