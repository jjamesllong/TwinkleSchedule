package com.keycome.twinkleschedule.presentation.configuration

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.keycome.twinkleschedule.BaseFragment
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.databinding.FragmentGuideScheduleBinding
import com.keycome.twinkleschedule.databinding.ViewToolbarLayoutBinding

class GuideScheduleFragment : BaseFragment<FragmentGuideScheduleBinding>() {

    override fun supportBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentGuideScheduleBinding {
        return FragmentGuideScheduleBinding.inflate(inflater, container, false)
    }

    override fun supportToolbar(title: Array<Int>): ViewToolbarLayoutBinding {
        title[0] = R.string.guideScheduleFragmentLabel
        return binding.fragmentAddScheduleToolbar
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val intent = requireActivity().intent
        val k = intent.getStringExtra(ConfigurationActivity.NAV_KEY_TO_MANAGE_SCHEDULE)
        val v = ConfigurationActivity.NAV_VALUE_TO_MANAGE_SCHEDULE
        val action = R.id.action_guideScheduleFragment_to_manageScheduleFragment
        if (k == v) findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.manualConfiguration.setOnClickListener {
            findNavController().navigate(
                R.id.action_guideScheduleFragment_to_editScheduleFragment
            )
        }
    }
}