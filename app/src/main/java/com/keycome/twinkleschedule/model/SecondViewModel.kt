package com.keycome.twinkleschedule.model

import com.keycome.twinkleschedule.base.BaseViewModel
import com.keycome.twinkleschedule.pipette.SecondPipette

class SecondViewModel : BaseViewModel() {

    private val pipette: SecondPipette by pipettes()

    override fun onInit() {
    }

    override suspend fun onAsync() {
    }
}