package com.keycome.twinkleschedule.fragment

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.keycome.twinkleschedule.base.BaseFragment
import com.keycome.twinkleschedule.databinding.FragmentCourseColorBinding
import com.keycome.twinkleschedule.delivery.Pipette
import com.keycome.twinkleschedule.delivery.Pipette.distribute
import com.keycome.twinkleschedule.extension.viewbindings.acquire
import com.keycome.twinkleschedule.extension.strings.toIntFromHex
import com.keycome.twinkleschedule.util.const.KEY_COURSE_COLOR
import kotlinx.coroutines.launch

class CourseColorFragment : BaseFragment() {

    private var _binding: FragmentCourseColorBinding? = null
    val binding get() = _binding.acquire()

    private val navController by lazy { findNavController() }

    private val action: (Int) -> Unit = { colorInt ->
        (binding.fragmentCourseColorColorPreview.background as? GradientDrawable)?.also { b ->
            b.setColor(colorInt)
        }
        selectedColor = colorInt
    }

    private val colorsList: List<List<String>> = listOf(
        listOf(
            "FF002FA7",
            "FFFBD26A",
            "FF470024",
            "FF01847F",
            "FF492D22",
            "FF003153",
            "FFD44848",
        ),
        listOf(
            "FFFFFF99",
            "FFCCFFFF",
            "FFFFCCCC",
            "FFFF9966",
            "FFCCCCFF",
            "FFFF6666",
            "FFCCCCCC",
            "FFCCFFCC",
            "FFFFCC99",
            "FF99CC99",
            "FF99CCCC",
            "FF99CCFF"
        )
    )

    private var selectedColor = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentCourseColorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedColor = savedInstanceState?.getInt(
            KEY_COURSE_COLOR
        ) ?: arguments?.getInt(
            KEY_COURSE_COLOR
        ) ?: 0

        binding.fragmentCourseColorColorPreview.background = GradientDrawable().apply {
            val radius = (16 * requireContext().resources.displayMetrics.density + 0.5f)
            cornerRadius = radius
            setColor(selectedColor)
        }
        binding.fragmentCourseColorNavImage.setOnClickListener {
            navController.navigateUp()
        }
        binding.fragmentCourseColorCheckImage.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                Pipette.forInt.distribute(KEY_COURSE_COLOR) { selectedColor }
                navController.navigateUp()
            }
        }
        binding.fragmentCourseColorViewPager.adapter = createTabAdapter()

        TabLayoutMediator(
            binding.fragmentCourseColorTabBar,
            binding.fragmentCourseColorViewPager
        ) { tab, position ->
            when (position) {
                0 -> tab.text = "第一栏"
                1 -> tab.text = "第二栏"
            }
        }.attach()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_COURSE_COLOR, selectedColor)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createTabAdapter() = object : FragmentStateAdapter(this) {

        private val fragments = colorsList.map { colors ->
            ColorsFragment.newInstance(IntArray(colors.size) { i -> colors[i].toIntFromHex() })
        }

        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }

    }

    fun getAction(): (Int) -> Unit {
        return action
    }
}