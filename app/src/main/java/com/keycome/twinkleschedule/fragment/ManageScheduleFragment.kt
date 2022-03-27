package com.keycome.twinkleschedule.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.adapter.ScheduleListAdapter
import com.keycome.twinkleschedule.base.BaseFragment
import com.keycome.twinkleschedule.databinding.FragmentManageScheduleBinding
import com.keycome.twinkleschedule.delivery.Pipette
import com.keycome.twinkleschedule.dialog.ScheduleDetailsDialog
import com.keycome.twinkleschedule.model.ManageScheduleViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter

class ManageScheduleFragment : BaseFragment() {

    private var _binding: FragmentManageScheduleBinding? = null
    val binding get() = _binding.acquire()

    val viewModel by viewModels<ManageScheduleViewModel>()

    private val navController by lazy { findNavController() }

    private val scheduleListAdapter by lazy { ScheduleListAdapter(adapterEvent) }

    private val adapterEvent: (Int) -> Unit = { position ->
        viewModel.getScheduleIdByIndex(position)?.let { id ->
            navController.navigate(
                R.id.action_manageScheduleFragment_to_scheduleDetailsDialog,
                Bundle().apply { putLong(ScheduleDetailsDialog.scheduleIdKey, id) }
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageScheduleBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentManageScheduleRecyclerView.adapter = scheduleListAdapter
        binding.fragmentManageScheduleRecyclerView.layoutManager =
            LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
        binding.fragmentManageScheduleToolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }
        viewModel.liveScheduleList.observe(viewLifecycleOwner) {
            scheduleListAdapter.submitList(it)
        }
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            Pipette.pipetteForString.filter {
                it.first == "schedule_operation"
            }.collect {
                if (it.second == "delete") {
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