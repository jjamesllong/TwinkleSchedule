package com.keycome.twinkleschedule.extension

import androidx.viewbinding.ViewBinding

fun interface BindingWrapper<VB : ViewBinding> {

    fun onBind(binding: VB)

}