package com.keycome.twinkleschedule.presentation.configuration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.keycome.twinkleschedule.App
import com.keycome.twinkleschedule.BaseFragment
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.databinding.FragmentManageScheduleBinding
import com.keycome.twinkleschedule.databinding.ViewToolbarLayoutBinding
import com.keycome.twinkleschedule.record.DISPLAY_SCHEDULE_ID_DEFAULT_VALUE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ManageScheduleFragment : BaseFragment<FragmentManageScheduleBinding>() {

    private val activityViewModel by activityViewModels<ConfigurationViewModel>()

    private var optionsLayout: LinearLayout? = null
        get() {
            return field ?: with(binding.stubManageScheduleOptions.inflate() as LinearLayout) {
                visibility = View.INVISIBLE
                for (v in children) {
                    v.setOnClickListener {
                        val position = tag as Int
                        when (it.id) {
                            R.id.manage_schedule_option_delete -> {
                                activityViewModel.allScheduleLive.value?.let { scheduleList ->
                                    App.applicationScope.launch(Dispatchers.IO) {
                                        activityViewModel.deleteSchedule(scheduleList[position])
                                    }
                                }
                            }
                            R.id.manage_schedule_option_modify -> {
                                val s1 = activityViewModel.liveSchedule
                                val s2 = activityViewModel.allScheduleLive.value?.get(position)
                                s1.value = s2 ?: throw Exception()
                                val direction = ManageScheduleFragmentDirections
                                findNavController().navigate(
                                    direction.actionManageScheduleFragmentToEditScheduleFragment(
                                        isModify = true
                                    )
                                )
                            }
                            R.id.manage_schedule_option_main -> {
                                val s = activityViewModel.allScheduleLive.value?.get(position)
                                val id = s?.scheduleId ?: DISPLAY_SCHEDULE_ID_DEFAULT_VALUE
                                activityViewModel.displaySchedule(id)
                                requireActivity().finish()
                            }
                        }
                    }
                }
                field = this
                field
            }
        }

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

        val manageScheduleAdapter = ManageScheduleAdapter(itemClickListener)
        binding.fragmentManageScheduleRecyclerView.apply {
            adapter = manageScheduleAdapter
            layoutManager = LinearLayoutManager(context)
        }
        activityViewModel.allScheduleLive.observe(viewLifecycleOwner) {
            manageScheduleAdapter.submitList(it)
        }
    }

    private val itemClickListener: (position: Int) -> Unit = {
        this.optionsLayout?.run {
            visibility = if (isVisible) View.GONE else {
                tag = it
                View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        optionsLayout = null
    }
}