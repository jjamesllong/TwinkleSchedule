package com.keycome.twinkleschedule.fragment

import com.keycome.twinkleschedule.base.BindingFragment
import com.keycome.twinkleschedule.databinding.FragmentSettingsBinding
import com.keycome.twinkleschedule.design.SettingsDesign

class SettingsFragment : BindingFragment<FragmentSettingsBinding, SettingsDesign>() {

    override fun doBind(): FragmentSettingsBinding {
        return doFragmentBinding { inflater, container ->
            FragmentSettingsBinding.inflate(
                inflater,
                container,
                attachToParentFalse
            )
        }
    }

    override fun supportBindingDesign(): SettingsDesign {
        return SettingsDesign()
    }

    override fun onInit() {

    }

    override suspend fun onAsync() {

    }
}