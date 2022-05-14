package com.keycome.twinkleschedule.extension.activity

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModelProvider
import com.keycome.twinkleschedule.base.BaseActivity
import com.keycome.twinkleschedule.base.BaseViewModel
import com.keycome.twinkleschedule.extension.ViewModelDelegate

@MainThread
inline fun <reified VM : BaseViewModel> BaseActivity.viewModelDelegate(
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
) = ViewModelDelegate.createViewModelDelegate(
    VM::class,
    { viewModelStore },
    factoryProducer ?: { defaultViewModelProviderFactory }
)