package com.keycome.twinkleschedule.presentation.display

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.keycome.twinkleschedule.custom.PopupMenu
import com.keycome.twinkleschedule.custom.courseschedule.toCourseTable
import com.keycome.twinkleschedule.custom.courseschedule.toDayTable
import com.keycome.twinkleschedule.custom.courseschedule.toSectionTable
import com.keycome.twinkleschedule.databinding.FragmentDisplayCoursesBinding
import com.keycome.twinkleschedule.databinding.ViewFragmentDisplayPopupMenuBinding
import com.keycome.twinkleschedule.presentation.configuration.ConfigurationActivity
import com.keycome.twinkleschedule.presentation.record.RecordActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DisplayCoursesFragment : Fragment() {

    private lateinit var binding: FragmentDisplayCoursesBinding
    private val viewModel by viewModels<DisplayCourseViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDisplayCoursesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.liveCourseSchedule.observe(viewLifecycleOwner) {
                binding.dayRecyclerView.toDayTable(it.schedule)
                binding.sectionRecyclerView.toSectionTable(it.schedule)
                binding.courseRecyclerView.toCourseTable(
                    viewLifecycleOwner,
                    it.schedule,
                    it.courseList
                )
            }
        }

        binding.menuButton.setOnClickListener {
            onCreatePopupMenu().showAsDropDown(it)
        }
    }

    private fun onCreatePopupMenu(): PopupMenu {
        val binding = ViewFragmentDisplayPopupMenuBinding.inflate(
            LayoutInflater.from(context),
            null,
            false
        )
        return PopupMenu(requireContext(), binding.root) {
            when (it) {
                binding.toConfigurationMenuItem ->
                    startActivity(Intent(requireContext(), ConfigurationActivity::class.java))
                binding.toRecordMenuItem ->
                    startActivity(Intent(requireContext(), RecordActivity::class.java))
            }
            dismiss()
        }
    }
}