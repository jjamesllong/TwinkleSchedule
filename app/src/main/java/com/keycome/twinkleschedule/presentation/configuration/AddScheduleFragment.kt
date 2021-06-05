package com.keycome.twinkleschedule.presentation.configuration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.custom.CustomDialog
import com.keycome.twinkleschedule.databinding.FragmentAddScheduleBinding

class AddScheduleFragment : Fragment() {
    private lateinit var binding: FragmentAddScheduleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button.setOnClickListener {
            findNavController().navigate(
                R.id.action_addScheduleFragment_to_editScheduleFragment
            )
        }
        val l = mutableListOf<String>()
        val range = 0..100
        range.forEach {
            l.add(it.toString())
        }

        binding.simplePickerView.setData(l, 50)
    }
}