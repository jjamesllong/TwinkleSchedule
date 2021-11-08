package com.keycome.twinkleschedule.presentation.display

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
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

    private var _binding: FragmentDisplayCoursesBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<DisplayViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDisplayCoursesBinding.inflate(inflater, container, false)
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
                ) { c ->
                    val direction = DisplayCoursesFragmentDirections
                        .actionDisplayCoursesFragmentToCourseDetailDialogFragment(c.courseId)
                    findNavController().navigate(direction)
                }
            }
        }

        binding.menuButton.setOnClickListener {
            onCreatePopupMenu().showAsDropDown(it)
        }
    }

    private fun onCreatePopupMenu(): PopupMenu {
        val menuBinding = ViewFragmentDisplayPopupMenuBinding.inflate(
            LayoutInflater.from(context),
            null,
            false
        )
        return PopupMenu(requireContext(), menuBinding.root) {
            when (it) {
                menuBinding.toConfigurationMenuItem ->
                    startActivity(Intent(requireContext(), ConfigurationActivity::class.java))
                menuBinding.toRecordMenuItem ->
                    startActivity(Intent(requireContext(), RecordActivity::class.java))
                menuBinding.toManageScheduleMenuItem -> {
                    val intent = Intent(requireContext(), ConfigurationActivity::class.java)
                    intent.putExtra(
                        ConfigurationActivity.NAV_KEY_TO_MANAGE,
                        ConfigurationActivity.NAV_VALUE_TO_MANAGE
                    )
                    startActivity(intent)
                }
            }
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}