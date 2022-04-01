package com.keycome.twinkleschedule.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.adapter.ScheduleListAdapter
import com.keycome.twinkleschedule.base.BaseFragment
import com.keycome.twinkleschedule.databinding.FragmentScheduleListBinding
import com.keycome.twinkleschedule.extension.removeObservers
import com.keycome.twinkleschedule.model.ScheduleListViewModel
import kotlinx.coroutines.launch

class SelectToAddCourseFragment : BaseFragment() {

    private var _binding: FragmentScheduleListBinding? = null
    val binding get() = _binding.acquire()

    private val viewModel by viewModels<ScheduleListViewModel>()

    private val navController by lazy { findNavController() }

    private val event: (Int) -> Unit = { position: Int ->
        viewModel.getScheduleIdByIndex(position)?.let { id ->
            navController.navigate(
                R.id.action_selectToAddCourseFragment_to_editCourseFragment,
                Bundle().apply {
                    putLong(EditCourseFragment.scheduleIdKey, id)
                }
            )
        }
    }

    private val adapter by lazy { ScheduleListAdapter(event) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleListBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentScheduleListToolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }
        binding.fragmentScheduleListRecyclerView.adapter = adapter
        binding.fragmentScheduleListRecyclerView.layoutManager =
            LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
        viewModel.liveScheduleList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        lifecycleScope.launch {
            adapter.removeObservers()
        }
        _binding = null
    }

}