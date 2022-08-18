package com.keycome.twinkleschedule.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.adapter.ScheduleListAdapter
import com.keycome.twinkleschedule.base.BaseFragment
import com.keycome.twinkleschedule.databinding.FragmentScheduleListBinding
import com.keycome.twinkleschedule.model.ScheduleListViewModel
import com.keycome.twinkleschedule.util.const.KEY_SCHEDULE_END_DAY
import com.keycome.twinkleschedule.util.const.KEY_SCHEDULE_END_SECTION
import com.keycome.twinkleschedule.util.const.KEY_SCHEDULE_END_WEEK
import com.keycome.twinkleschedule.util.const.KEY_SCHEDULE_ID

class SelectToAddCourseFragment : BaseFragment() {

    private var _binding: FragmentScheduleListBinding? = null
    val binding get() = _binding.acquire()

    private val viewModel by viewModels<ScheduleListViewModel>()

    private val navController by lazy { findNavController() }

    private val event: (Int) -> Unit = { position: Int ->
        viewModel.getScheduleByIndex(position)?.let { s ->
            navController.navigate(
                R.id.action_selectToAddCourseFragment_to_editCourseFragment,
                Bundle().apply {
                    putBoolean(EditCourseFragment.KEY_IS_UPDATE, false)
                    putLong(KEY_SCHEDULE_ID, s.scheduleId)
                    putInt(KEY_SCHEDULE_END_DAY, s.endDay)
                    putInt(KEY_SCHEDULE_END_SECTION, s.endSection)
                    putInt(KEY_SCHEDULE_END_WEEK, s.endWeek)
                }
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleListBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentScheduleListToolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }
        val adapter = ScheduleListAdapter(event)
        binding.fragmentScheduleListRecyclerView.adapter = adapter
        binding.fragmentScheduleListRecyclerView.layoutManager =
            LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
        viewModel.liveScheduleList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}