package com.keycome.twinkleschedule.base

import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

abstract class Design {

    val coroutineScope = CoroutineScope(Dispatchers.Unconfined)

    abstract val rootView: View

    protected inline fun <reified P : Pipette> pipettes() = Pipette.pipettes<P>()

}