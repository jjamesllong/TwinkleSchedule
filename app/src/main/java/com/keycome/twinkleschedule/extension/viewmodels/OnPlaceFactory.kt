package com.keycome.twinkleschedule.extension.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.keycome.twinkleschedule.base.BaseViewModel
import kotlinx.coroutines.launch

object OnPlaceFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = try {
            modelClass.newInstance()
        } catch (e: InstantiationException) {
            throw RuntimeException("Cannot create an instance of $modelClass", e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException("Cannot create an instance of $modelClass", e)
        }
        (viewModel as? BaseViewModel)?.also {
            it.viewModelScope.launch {
                it.onPlace()
            }
        }
        return viewModel
    }
}