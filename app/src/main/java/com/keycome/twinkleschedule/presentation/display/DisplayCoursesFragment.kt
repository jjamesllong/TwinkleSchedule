package com.keycome.twinkleschedule.presentation.display

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
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
        val gridLayoutManager = GridLayoutManager(
            context,
            TestData.courseSchedule.courses,
            GridLayoutManager.HORIZONTAL,
            false
        )
        binding.courseRecyclerView.apply {
            layoutManager = gridLayoutManager
            adapter = CourseAdapter()
        }
    }
}