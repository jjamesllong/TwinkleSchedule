package com.keycome.twinkleschedule.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*

abstract class BaseFragment : Fragment() {

    val coroutineScope = MainScope()

    private var design: Design? = null

    abstract suspend fun main()

    private var defer: (suspend () -> Unit)? = null
    private var deferRunning = false
    protected fun defer(operation: suspend () -> Unit) {
        this.defer = operation
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        design = supportDesign(inflater, container)
        return design?.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenStarted { }
        coroutineScope.launch {
            main()
        }
    }

    override fun onDestroyView() {
        Log.d("BaseFragment", "${javaClass.simpleName} onDestroy")
        design?.coroutineScope?.cancel()
        coroutineScope.cancel()
        super.onDestroyView()
        design = null
    }

    override fun onDestroy() {

        if (deferRunning) return

        deferRunning = true

        coroutineScope.launch {
            try {
                defer?.invoke()
            } finally {
                withContext(NonCancellable) {
                    super.onDestroy()
                }
            }
        }
    }

    open fun supportDesign(inflater: LayoutInflater, container: ViewGroup?): Design? {
        return null
    }

    protected inline fun <reified P : Pipette> pipettes() = Pipette.pipettes<P>()

}