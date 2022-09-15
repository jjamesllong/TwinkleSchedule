package com.keycome.twinkleschedule.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.adapter.ScheduleListAdapter
import com.keycome.twinkleschedule.base.BaseFragment
import com.keycome.twinkleschedule.databinding.FragmentScheduleListBinding
import com.keycome.twinkleschedule.delivery.Pipette
import com.keycome.twinkleschedule.delivery.Pipette.subscribe
import com.keycome.twinkleschedule.dialog.ScheduleDetailsDialog
import com.keycome.twinkleschedule.extension.viewbindings.acquire
import com.keycome.twinkleschedule.viewmodel.ScheduleListViewModel

class SelectToManageScheduleFragment : BaseFragment() {

    private var _binding: FragmentScheduleListBinding? = null
    val binding get() = _binding.acquire()

    val viewModel by viewModels<ScheduleListViewModel>()

    private val navController by lazy { findNavController() }

    private val adapterEvent: (Int) -> Unit = { position ->
        viewModel.getScheduleByIndex(position)?.also { s ->
            navController.navigate(
                R.id.action_manageScheduleFragment_to_scheduleDetailsDialog,
                Bundle().apply { putLong(ScheduleDetailsDialog.KEY_SCHEDULE, s.scheduleId) }
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
        val scheduleListAdapter = ScheduleListAdapter(adapterEvent)
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
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            Pipette.forString.subscribe(ScheduleDetailsDialog.KEY_SCHEDULE_OPERATION) {
                if (it == ScheduleDetailsDialog.DELETE) {
                    viewModel.queryScheduleList()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}