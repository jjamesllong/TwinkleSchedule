package com.keycome.twinkleschedule.base

import android.view.View
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.viewbinding.ViewBinding
import com.keycome.twinkleschedule.extension.fragment.activityViewModelDelegate
import com.keycome.twinkleschedule.extension.fragment.viewModelDelegate
import com.keycome.twinkleschedule.util.TextColor

abstract class BaseFragment : Fragment() {

    protected inline fun <reified VM : BaseViewModel> viewModels(
        noinline ownerProducer: () -> ViewModelStoreOwner = { this },
        noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
    ) = viewModelDelegate<VM>(ownerProducer, factoryProducer)

    protected inline fun <reified VM : BaseViewModel> activityViewModels(
        noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
    ) = activityViewModelDelegate<VM>(factoryProducer)

    fun setStatusBarAppearance(view: View, color: TextColor) {
        val windowInsetsController = ViewCompat.getWindowInsetsController(view)
        windowInsetsController?.isAppearanceLightStatusBars = when (color) {
            TextColor.Light -> false
            TextColor.Dark -> true
        }
    }

    inline fun <reified VB : ViewBinding> VB?.acquire(): VB {
        return this ?: throw IllegalStateException(
            "request an instance of ${VB::class.qualifiedName} " +
                    "outside the lifecycle of onCreateView() and onDestroyView()"
        )
    }

}