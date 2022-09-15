package com.keycome.twinkleschedule.extension.activities

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModelLazy
import com.keycome.twinkleschedule.base.BaseActivity
import com.keycome.twinkleschedule.base.BaseViewModel
import com.keycome.twinkleschedule.extension.viewmodels.OnPlaceFactory

@MainThread
inline fun <reified VM : BaseViewModel> BaseActivity.viewModelDelegate(): Lazy<VM> {
    val clazz = VM::class
    val storeProducer = { viewModelStore }
    val factoryProducer = { OnPlaceFactory }
    return ViewModelLazy(clazz, storeProducer, factoryProducer)
}
