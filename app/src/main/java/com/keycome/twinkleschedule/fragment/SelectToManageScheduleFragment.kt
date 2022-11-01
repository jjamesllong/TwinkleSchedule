package com.keycome.twinkleschedule.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.adapter.ScheduleListAdapter
import com.keycome.twinkleschedule.base.BaseFragment
import com.keycome.twinkleschedule.databinding.FragmentScheduleListBinding
import com.keycome.twinkleschedule.extension.viewbindings.acquire
import com.keycome.twinkleschedule.util.const.KEY_SCHEDULE_ID
import com.keycome.twinkleschedule.viewmodel.ScheduleListViewModel

class SelectToManageScheduleFragment : BaseFragment() {

    private var _binding: FragmentScheduleListBinding? = null
    val binding get() = _binding.acquire()

    val viewModel by viewModels<ScheduleListViewModel>()

    private val navController by lazy { findNavController() }

    private val itemClickCallback: (Int) -> Unit = { position ->
        viewModel.getScheduleByIndex(position)?.also { schedule ->
            navController.navigate(
                R.id.action_manageScheduleFragment_to_scheduleDetailsDialog,
                Bundle().apply { putLong(KEY_SCHEDULE_ID, schedule.scheduleId) }
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
        val scheduleListAdapter = ScheduleListAdapter(itemClickCallback)
        binding.fragmentScheduleListRecyclerView.adapter = scheduleListAdapter
        binding.fragmentScheduleListToolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }
        viewModel.liveUsingScheduleId.observe(viewLifecycleOwner) {
            scheduleListAdapter.setUsingScheduleId(it)
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