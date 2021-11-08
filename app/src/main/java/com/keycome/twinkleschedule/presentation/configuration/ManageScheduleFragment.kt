package com.keycome.twinkleschedule.presentation.configuration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.keycome.twinkleschedule.BaseFragment
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.databinding.FragmentManageScheduleBinding
import com.keycome.twinkleschedule.databinding.ViewToolbarLayoutBinding

class ManageScheduleFragment : BaseFragment<FragmentManageScheduleBinding>() {

    private val activityViewModel by activityViewModels<ConfigurationViewModel>()

    override fun supportBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentManageScheduleBinding {
        return FragmentManageScheduleBinding.inflate(inflater, container, false)
    }

    override fun supportToolbar(title: Array<Int>): ViewToolbarLayoutBinding {
        title[0] = R.string.manageScheduleFragmentLabel
        return binding.fragmentManageScheduleToolbar
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val manageScheduleAdapter = ManageScheduleAdapter()
        binding.fragmentManageScheduleRecyclerView.apply {
            adapter = manageScheduleAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        activityViewModel.allScheduleLive.observe(viewLifecycleOwner) {
            manageScheduleAdapter.submitList(it)
        }
    }
}