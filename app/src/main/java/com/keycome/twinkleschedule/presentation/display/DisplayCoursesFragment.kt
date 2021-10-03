package com.keycome.twinkleschedule.presentation.display

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.custom.CustomPopupMenu
import com.keycome.twinkleschedule.custom.courseschedule.toCourseTable
import com.keycome.twinkleschedule.databinding.FragmentDisplayCoursesBinding
import com.keycome.twinkleschedule.presentation.configuration.ConfigurationActivity
import com.keycome.twinkleschedule.presentation.record.RecordActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DisplayCoursesFragment : Fragment() {
    private lateinit var binding: FragmentDisplayCoursesBinding
    private var popupMenu: PopupWindow? = null
    private val viewModel by viewModels<DisplayCourseViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDisplayCoursesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.liveCourseSchedule.observe(viewLifecycleOwner) {
                binding.courseRecyclerView.toCourseTable(
                    viewLifecycleOwner,
                    it.schedule,
                    it.courseList
                )
            }
        }

        binding.menuButton.setOnClickListener { v ->
            if (popupMenu == null) onCreatePopupMenu()
            popupMenu!!.run { if (isShowing) dismiss() else showAsDropDown(v) }
        }
    }

    @SuppressLint("InflateParams")
    private fun onCreatePopupMenu() {
        val content = LayoutInflater.from(context).inflate(
            R.layout.view_popup_menu, null, false
        ) as LinearLayout
        popupMenu = CustomPopupMenu(requireContext(), content).apply {
            setOnItemSelectedListener {
                when (it.id) {
                    R.id.add_schedule_item -> {
                        startActivity(Intent(requireContext(), ConfigurationActivity::class.java))
                    }
                    R.id.record_course_item -> {
                        startActivity(Intent(requireContext(), RecordActivity::class.java))
                    }
                }
            }
        }
    }
}