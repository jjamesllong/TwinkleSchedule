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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.custom.CustomPopupMenu
import com.keycome.twinkleschedule.database.TestData
import com.keycome.twinkleschedule.databinding.FragmentDisplayCoursesBinding
import com.keycome.twinkleschedule.presentation.configuration.ConfigurationActivity

class DisplayCoursesFragment : Fragment() {
    private lateinit var binding: FragmentDisplayCoursesBinding
    private var popupMenu: PopupWindow? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDisplayCoursesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val coursesArray = BlockFactory().convertEntityToBlock()
        val gridLayoutManager = GridLayoutManager(
            context,
            TestData.courseSchedule.courses,
            GridLayoutManager.HORIZONTAL,
            false
        ).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return coursesArray[position].spanSize
                }
            }
        }
        val courseAdapter = CourseAdapter(5).apply {
            courseBlockArray = coursesArray
            courseEntityArray = TestData.courseArray
        }
        binding.courseRecyclerView.apply {
            layoutManager = gridLayoutManager
            adapter = courseAdapter
        }
        binding.imageButton.setOnClickListener { v ->
            if (popupMenu == null) onCreatePopupMenu()
            popupMenu!!.run { if (isShowing) dismiss() else showAsDropDown(v) }
        }
    }

    @SuppressLint("InflateParams")
    private fun onCreatePopupMenu() {
        val content = LayoutInflater.from(context).inflate(
            R.layout.popup_menu, null, false
        ) as LinearLayout
        popupMenu = CustomPopupMenu(requireContext(), content).apply {
            setOnItemSelectedListener {
                when (it.id) {
                    R.id.add_schedule_item -> {
                        startActivity(Intent(requireContext(), ConfigurationActivity::class.java))
                    }
                }
            }
        }
    }
}