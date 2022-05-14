package com.keycome.twinkleschedule.extension

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewModelScope
import com.keycome.twinkleschedule.base.BaseViewModel
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class ViewModelDelegate<VM : BaseViewModel>(
    private val viewModelClass: KClass<VM>,
    private val storeProducer: () -> ViewModelStore,
    private val factoryProducer: () -> ViewModelProvider.Factory
) : Lazy<VM> {

    private var cache: VM? = null

    override val value: VM
        get() {
            val viewModel = cache
            return if (viewModel == null) {
                val factory = factoryProducer()
                val store = storeProducer()
                ViewModelProvider(store, factory).get(viewModelClass.java).also {
                    cache = it
                    it.viewModelScope.launch { it.onPlace() }
                }
            } else {
                viewModel
            }
        }

    override fun isInitialized() = cache != null

    companion object {

        @MainThread
        fun <VM : BaseViewModel> createViewModelDelegate(
            viewModelClass: KClass<VM>,
            storeProducer: () -> ViewModelStore,
            factoryProducer: () -> ViewModelProvider.Factory
        ): Lazy<VM> = ViewModelDelegate(viewModelClass, storeProducer, factoryProducer)
    }
}