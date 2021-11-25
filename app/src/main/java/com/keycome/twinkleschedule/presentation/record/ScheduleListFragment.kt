package com.keycome.twinkleschedule.presentation.record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.keycome.twinkleschedule.BaseFragment
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.databinding.FragmentScheduleListBinding
import com.keycome.twinkleschedule.databinding.ViewToolbarLayoutBinding

class ScheduleListFragment : BaseFragment<FragmentScheduleListBinding>() {

    private val viewModel by activityViewModels<RecordViewModel>()

    private val isManage: Boolean by lazy {
        val bundle = requireActivity().intent.extras
        return@lazy bundle?.getBoolean(RecordActivity.NAV_KEY_TO_MANAGE_COURSE) ?: false
    }

    override fun supportBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentScheduleListBinding {
        return FragmentScheduleListBinding.inflate(inflater, container, false)
    }

    override fun supportToolbar(title: Array<Int>): ViewToolbarLayoutBinding {
        title[0] = R.string.scheduleListFragmentLabel
        return binding.fragmentScheduleListToolbar
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scheduleListAdapter = ScheduleListAdapter(isManage)
        binding.fragmentScheduleListRecyclerView.apply {
            adapter = scheduleListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        viewModel.liveScheduleList.observe(viewLifecycleOwner) {
            scheduleListAdapter.submitList(it)
        }
    }
}