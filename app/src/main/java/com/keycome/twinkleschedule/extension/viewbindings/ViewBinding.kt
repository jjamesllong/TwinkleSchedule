package com.keycome.twinkleschedule.extension.viewbindings

import androidx.viewbinding.ViewBinding

inline fun <reified VB : ViewBinding> VB?.acquire(): VB {
    return this ?: throw IllegalStateException(
        "request an instance of ${VB::class.qualifiedName} " +
                "outside the lifecycle of onCreateView() and onDestroyView()"
    )
}