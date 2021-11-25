package com.keycome.twinkleschedule.extension

import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlin.reflect.KProperty

fun <T> Fragment.liveViewManager(initializer: () -> T) =
    LiveViewManagerImpl(this, initializer)

class LiveViewManagerImpl<T>(
    private val fragment: Fragment,
    private val initializer: () -> T
) : DefaultLifecycleObserver {

    private var cached: T? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        fragment.viewLifecycleOwner.lifecycle.addObserver(this)
        return cached ?: throw IllegalStateException(
            "this property can only invoked between onCreate and onDestroy"
        )
    }

    override fun onCreate(owner: LifecycleOwner) {
        cached = initializer()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        fragment.viewLifecycleOwner.lifecycle.removeObserver(this)
        cached = null
    }
}