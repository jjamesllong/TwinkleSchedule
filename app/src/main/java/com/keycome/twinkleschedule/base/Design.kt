package com.keycome.twinkleschedule.base

import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

abstract class Design<VB : ViewBinding> : CoroutineScope by CoroutineScope(Dispatchers.Unconfined) {

    abstract val binding: VB

    protected inline fun <reified P : Pipette> pipettes() = Pipette.pipettes<P>()

}