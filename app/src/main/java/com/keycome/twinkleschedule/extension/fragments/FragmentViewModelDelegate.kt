package com.keycome.twinkleschedule.extension.fragments

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.keycome.twinkleschedule.base.BaseViewModel
import com.keycome.twinkleschedule.extension.ViewModelDelegate

@MainThread
inline fun <reified VM : BaseViewModel> Fragment.viewModelDelegate(
    noinline ownerProducer: () -> ViewModelStoreOwner = { this },
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
) = ViewModelDelegate.createViewModelDelegate(
    VM::class,
    { ownerProducer().viewModelStore },
    factoryProducer ?: { defaultViewModelProviderFactory }
)

@MainThread
inline fun <reified VM : BaseViewModel> Fragment.activityViewModelDelegate(
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
) = ViewModelDelegate.createViewModelDelegate(
    VM::class,
    { requireActivity().viewModelStore },
    factoryProducer ?: { requireActivity().defaultViewModelProviderFactory }
)