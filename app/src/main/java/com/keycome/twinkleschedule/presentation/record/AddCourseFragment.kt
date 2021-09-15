package com.keycome.twinkleschedule.presentation.record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.keycome.twinkleschedule.BaseFragment
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.databinding.CustomToolbarLayoutBinding
import com.keycome.twinkleschedule.databinding.FragmentAddCourseBinding
import com.keycome.twinkleschedule.extension.toast

class AddCourseFragment : BaseFragment<FragmentAddCourseBinding>() {

    private val viewModel by activityViewModels<RecordViewModel>()
    private val args by navArgs<AddCourseFragmentArgs>()

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

        val addCourseAdapter = AddCourseAdapter(viewModel)
        binding.editingCourseRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = addCourseAdapter
        }
        viewModel.liveCourseList.observe(viewLifecycleOwner) {
            addCourseAdapter.submitList(it)
        }
    }

}