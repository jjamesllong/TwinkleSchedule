package com.keycome.twinkleschedule.presentation.configuration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.keycome.twinkleschedule.BaseFragment
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.databinding.CustomToolbarLayoutBinding
import com.keycome.twinkleschedule.databinding.FragmentAddTimeLineBinding

class AddTimeLineFragment : BaseFragment<FragmentAddTimeLineBinding>() {

    private val viewModel: ConfigurationViewModel by viewModels()

    override fun deployBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddTimeLineBinding {
        return FragmentAddTimeLineBinding.inflate(inflater, container, false)
    }

    override fun supportToolbar(title: Array<Int>): CustomToolbarLayoutBinding {
        title[0] = R.string.timeLineFragmentLabel
        return binding.fragmentAddTimeLineToolbar
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val addTimeLineAdapter = AddTimeLineAdapter()
        binding.fragmentAddTimeLineRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext()).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            adapter = addTimeLineAdapter
        }
        viewModel.liveScheduleList.observe(viewLifecycleOwner) {
            val list = mutableListOf<String>()
            it.forEach { scheduleEntity ->
                list.add(scheduleEntity.name)
            }
            addTimeLineAdapter.submitList(list)
        }
    }
}