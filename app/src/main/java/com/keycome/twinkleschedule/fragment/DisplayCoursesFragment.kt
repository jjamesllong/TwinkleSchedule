package com.keycome.twinkleschedule.fragment

import com.keycome.twinkleschedule.base.BindingFragment
import com.keycome.twinkleschedule.databinding.FragmentDisplayCoursesBinding
import com.keycome.twinkleschedule.design.DisplayCoursesDesign
import com.keycome.twinkleschedule.model.DisplayCoursesViewModel
import com.keycome.twinkleschedule.pipette.DisplayCoursesPipette

class DisplayCoursesFragment :
    BindingFragment<FragmentDisplayCoursesBinding, DisplayCoursesDesign>() {

    val viewModel by fragmentViewModels<DisplayCoursesViewModel>()

    override fun doBind(): FragmentDisplayCoursesBinding {
        return doFragmentBinding { inflater, container ->
            FragmentDisplayCoursesBinding.inflate(
                inflater,
                container,
                attachToParentFalse
            )
        }
    }

    override fun supportBindingDesign(): DisplayCoursesDesign {
        return DisplayCoursesDesign(fragment = this)
    }

    override fun onInit() {
        viewModel.supportKey(DisplayCoursesPipette::class.simpleName!!)
    }

    override suspend fun onAsync() {

    }
}