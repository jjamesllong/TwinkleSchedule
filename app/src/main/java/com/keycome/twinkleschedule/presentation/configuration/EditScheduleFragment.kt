package com.keycome.twinkleschedule.presentation.configuration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.keycome.twinkleschedule.custom.CustomPopupTextField
import com.keycome.twinkleschedule.databinding.FragmentEditScheduleBinding
import com.keycome.twinkleschedule.extension.toast

class EditScheduleFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentEditScheduleBinding

    private val popupTextField: CustomPopupTextField by lazy {
        CustomPopupTextField(requireContext()).apply {
            onPositiveButtonPressed {
                toast("positive")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditScheduleBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        for (child in binding.linearLayout.children)
            child.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.editScheduleNameItem.id -> {
                popupTextField.show()
            }
            binding.editSchoolBeginDateItem.id -> {
                toast("world")
            }
            else -> toast("none")
        }
    }
}