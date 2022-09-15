package com.keycome.twinkleschedule.extension.fragments

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelLazy
import com.keycome.twinkleschedule.base.BaseViewModel
import com.keycome.twinkleschedule.extension.viewmodels.OnPlaceFactory

@MainThread
inline fun <reified VM : BaseViewModel> Fragment.viewModelDelegate(): Lazy<VM> {
    val clazz = VM::class
    val storeProducer = { viewModelStore }
    val factoryProducer = { OnPlaceFactory }
    return ViewModelLazy(clazz, storeProducer, factoryProducer)
}

@MainThread
inline fun <reified VM : BaseViewModel> Fragment.activityViewModelDelegate(): Lazy<VM> {
    val clazz = VM::class
    val storeProducer = { requireActivity().viewModelStore }
    val factoryProducer = { OnPlaceFactory }
    return ViewModelLazy(clazz, storeProducer, factoryProducer)
}