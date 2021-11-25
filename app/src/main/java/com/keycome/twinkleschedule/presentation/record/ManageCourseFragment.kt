package com.keycome.twinkleschedule.presentation.record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.keycome.twinkleschedule.BaseFragment
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.databinding.FragmentManageCourseBinding
import com.keycome.twinkleschedule.databinding.ViewToolbarLayoutBinding
import com.keycome.twinkleschedule.extension.liveViewManager

class ManageCourseFragment : BaseFragment<FragmentManageCourseBinding>() {

    private val activityViewModel: RecordViewModel by activityViewModels()

    private val args: ManageCourseFragmentArgs by navArgs()

    val optionsLayout: LinearLayout by liveViewManager {
        with(binding.stubManageCourseOptions.inflate() as LinearLayout) {
            visibility = View.INVISIBLE
            for (v in children) {
                v.setOnClickListener {
                    val index = tag as Int
                    when (v.id) {
                        R.id.manageCourseOptionDelete -> {
                            activityViewModel.liveQueriedCourse.value?.let {
                                activityViewModel.deleteQueriedCourse(it[index])
                            }
                        }
                        R.id.manageCourseOptionModify -> {}
                    }
                }
            }
            return@with this
        }
    }

    override fun supportBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentManageCourseBinding {
        return FragmentManageCourseBinding.inflate(inflater, container, false)
    }

    override fun supportToolbar(title: Array<Int>): ViewToolbarLayoutBinding {
        title[0] = R.string.manageCourseFragmentLabel
        return binding.fragmentManageCourseToolbar
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val manageCourseAdapter = ManageCourseAdapter(fragment = this)
        binding.manageCourseRecyclerView.apply {
            adapter = manageCourseAdapter
            layoutManager = LinearLayoutManager(context)
        }

        activityViewModel.requestQueriedCourse(args.scheduleId).observe(viewLifecycleOwner) {
            manageCourseAdapter.submitList(it)
        }
    }
}