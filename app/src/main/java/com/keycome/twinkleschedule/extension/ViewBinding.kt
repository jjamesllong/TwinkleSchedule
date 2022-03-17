package com.keycome.twinkleschedule.extension

import androidx.viewbinding.ViewBinding

inline fun <reified VB : ViewBinding> VB?.acquire(): VB {
    return this ?: throw IllegalStateException(
        "Hold an instance of ${VB::class.qualifiedName} " +
                "outside the lifecycle of onCreateView() and onDestroyView()"
    )
}