package com.keycome.twinkleschedule.presentation.record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.keycome.twinkleschedule.BaseFragment
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.databinding.CellScheduleTitleBinding
import com.keycome.twinkleschedule.databinding.CustomToolbarLayoutBinding
import com.keycome.twinkleschedule.databinding.FragmentScheduleListBinding
import com.keycome.twinkleschedule.extension.toast

class ScheduleListFragment : BaseFragment<FragmentScheduleListBinding>() {

    private val viewModel by activityViewModels<RecordViewModel>()

    override fun supportBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentScheduleListBinding {
        return FragmentScheduleListBinding.inflate(inflater, container, false)
    }

    override fun supportToolbar(title: Array<Int>): CustomToolbarLayoutBinding {
        title[0] = R.string.scheduleListFragmentLabel
        return binding.fragmentScheduleListToolbar
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scheduleListAdapter = ScheduleListAdapter()
        binding.fragmentScheduleListRecyclerView.apply {
            adapter = scheduleListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        viewModel.liveScheduleList.observe(viewLifecycleOwner) {
            scheduleListAdapter.submitList(it)
        }
    }

    val itemEventHandler = { binding: CellScheduleTitleBinding ->
        binding.root.setOnClickListener { toast("hello") }
    }
}