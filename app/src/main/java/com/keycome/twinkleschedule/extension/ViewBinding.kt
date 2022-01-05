package com.keycome.twinkleschedule.extension

import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

inline fun <VB : ViewBinding> VB.init(
    coroutineScope: CoroutineScope,
    crossinline block: (suspend VB.() -> Unit)
): VB {
    coroutineScope.launch {
        block()
    }
    return this
}

fun <VB : ViewBinding> viewBindings(
    coroutineScope: CoroutineScope,
    initializer: () -> VB,
    block: suspend VB.() -> Unit
): Lazy<VB> = ViewBindingImpl(coroutineScope, initializer, block)

private class ViewBindingImpl<VB : ViewBinding>(
    coroutineScope: CoroutineScope,
    initializer: () -> VB,
    block: suspend VB.() -> Unit
) : Lazy<VB> by lazy(initializer = {
    initializer().init(coroutineScope, block)
})