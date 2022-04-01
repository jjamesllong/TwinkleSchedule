package com.keycome.twinkleschedule.base

import android.view.View
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import com.keycome.twinkleschedule.delivery.*
import com.keycome.twinkleschedule.util.TextColor

abstract class BaseFragment : Fragment() {

    val companionViewModel by viewModels<CompanionViewModel>()

    inline fun emit(direction: String, action: PutBoundScope.() -> Unit) {
        if (direction.isBlank()) {
            throw Exception()
        }
        action(PutBoundScopeImpl(direction))
    }

    inline fun retrieve(direction: String, action: GetBoundScope.() -> Unit) {
        if (direction.isBlank()) {
            throw Exception()
        }
        companionViewModel.addDirection(direction)
        action(GetBoundScopeImpl(direction))
    }

    inline fun retrieveOnce(direction: String, action: GetBoundScope.() -> Unit) {
        if (direction.isBlank()) {
            throw Exception()
        }
        if (companionViewModel.firstPresent) {
            companionViewModel.addDirection(direction)
            action(GetBoundScopeImpl(direction))
            companionViewModel.firstPresent = false
        }
    }

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

    class CompanionViewModel : BaseViewModel() {

        private val directions = mutableSetOf<String>()

        override fun onRemove() {
            super.onRemove()
            directions.forEach { PostOffice.clearBounds(it) }
        }

        fun addDirection(direction: String) {
            directions.add(direction)
        }
    }
}