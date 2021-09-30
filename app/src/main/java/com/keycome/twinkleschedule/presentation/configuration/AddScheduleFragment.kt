package com.keycome.twinkleschedule.presentation.configuration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.keycome.twinkleschedule.App
import com.keycome.twinkleschedule.BaseFragment
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.databinding.CustomToolbarLayoutBinding
import com.keycome.twinkleschedule.databinding.FragmentAddScheduleBinding
import com.keycome.twinkleschedule.repository.Repository
import kotlinx.coroutines.launch

class AddScheduleFragment : BaseFragment<FragmentAddScheduleBinding>() {

    override fun supportBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddScheduleBinding {
        return FragmentAddScheduleBinding.inflate(inflater, container, false)
    }

    override fun supportToolbar(title: Array<Int>): CustomToolbarLayoutBinding {
        title[0] = R.string.addScheduleFragmentLabel
        return binding.fragmentAddScheduleToolbar
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
            findNavController().navigate(
                R.id.action_addScheduleFragment_to_editScheduleFragment
            )
        }
        binding.insertScheduleTestButton.setOnClickListener {
            App.applicationScope.launch {
                Repository.deleteAllSchedule()
            }
        }
    }
}