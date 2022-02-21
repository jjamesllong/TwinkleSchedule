package com.keycome.twinkleschedule.extension

import androidx.viewbinding.ViewBinding

fun interface BindingProvider<VB : ViewBinding> {

    fun doBind(): VB

}