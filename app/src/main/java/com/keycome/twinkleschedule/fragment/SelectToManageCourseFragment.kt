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
import com.keycome.twinkleschedule.extension.viewbindings.acquire
import com.keycome.twinkleschedule.viewmodel.ScheduleListViewModel
import com.keycome.twinkleschedule.util.const.KEY_SCHEDULE_ID

class SelectToManageCourseFragment : BaseFragment() {

    private var _binding: FragmentScheduleListBinding? = null
    private val binding get() = _binding.acquire()

    private val navController by lazy { findNavController() }

    private val viewModel by viewModels<ScheduleListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentScheduleListBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val scheduleListAdapter = ScheduleListAdapter {
            val s = viewModel.getScheduleByIndex(it)
            s?.also {
                navController.navigate(
                    R.id.action_selectToManageCourseFragment_to_courseListFragment,
                    Bundle().apply { putLong(KEY_SCHEDULE_ID, it.scheduleId) }
                )
            }
        }
        binding.fragmentScheduleListRecyclerView.adapter = scheduleListAdapter
        binding.fragmentScheduleListRecyclerView.layoutManager =
            LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
        binding.fragmentScheduleListToolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }
        viewModel.liveScheduleList.observe(viewLifecycleOwner) {
            scheduleListAdapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}