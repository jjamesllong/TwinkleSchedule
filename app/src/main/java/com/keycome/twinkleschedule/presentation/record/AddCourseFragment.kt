package com.keycome.twinkleschedule.presentation.record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.keycome.twinkleschedule.BaseFragment
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.databinding.CustomToolbarLayoutBinding
import com.keycome.twinkleschedule.databinding.FragmentAddCourseBinding

class AddCourseFragment : BaseFragment<FragmentAddCourseBinding>() {

    val viewModel by activityViewModels<RecordViewModel>()
    val args by navArgs<AddCourseFragmentArgs>()

    override fun supportBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddCourseBinding {
        return FragmentAddCourseBinding.inflate(inflater, container, false)
    }

    override fun supportToolbar(title: Array<Int>): CustomToolbarLayoutBinding {
        title[0] = R.string.addCourseFragmentLabel
        return binding.fragmentAddCourseToolbar
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val schedule = viewModel.liveScheduleList.value?.find { it.scheduleId == args.scheduleId }
        schedule?.let {
            binding.tvv1.text = it.scheduleId.toString()
            binding.tvv2.text = it.name
            binding.tvv3.text = it.schoolBeginDate.toDotDateString()
            binding.tvv4.text = it.dailyCourses.toString()
            binding.tvv5.text = it.weeklyEndDay.toString()
        }
    }

}