package com.keycome.twinkleschedule.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.viewbinding.ViewBinding
import com.keycome.twinkleschedule.extension.BindingProvider
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

abstract class BindingFragment<VB : ViewBinding, BD : BindingDesign<VB>> : BaseFragment(),
    BindingProvider<VB> {

    abstract fun onInit()

    abstract suspend fun onAsync()

    val coroutineScope = MainScope()

    var baseViewModel: Lazy<BaseViewModel>? = null

    val attachToParentFalse = false

    private var defer: (suspend () -> Unit)? = null
    private var deferRunning = false
    protected fun defer(operation: suspend () -> Unit) {
        this.defer = operation
    }

    var inflater: LayoutInflater? = null

    var container: ViewGroup? = null

    private var binding: VB? = null

    private var design: BD? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("BaseFragment", "${this::class.simpleName} onCreateView()")
        this.inflater = inflater
        this.container = container
        binding = doBind()
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("BaseFragment", "${this::class.simpleName} onViewCreated()")
        super.onViewCreated(view, savedInstanceState)
        onInit()
        design = supportBindingDesign()
        design?.performInit(viewLifecycleOwner)
        baseViewModel?.value?.performInit(viewLifecycleOwner)
        Log.d("BaseFragment", "binding is null: ${binding == null}")
        Log.d("BaseFragment", "design is null: ${design == null}")
        binding?.let { design?.onBind(it) }
        coroutineScope.launch {
            Log.d(TAG, "message from coroutineScope")
            onAsync()
        }
    }

    override fun onDestroyView() {
        Log.d("BaseFragment", "${javaClass.simpleName} onDestroyView()")
        coroutineScope.cancel()
        super.onDestroyView()
        inflater = null
        container = null
        design = null
        binding = null
    }

    override fun onDestroy() {
        Log.d("BaseFragment", "${javaClass.simpleName} onDestroy()")
        super.onDestroy()
        baseViewModel = null
    }

    inline fun doFragmentBinding(
        block: (inflater: LayoutInflater, container: ViewGroup?) -> VB
    ): VB {
        return block(inflater!!, container)
    }

    abstract fun supportBindingDesign(): BD

    protected inline fun <reified P : Pipette> pipettes() = Pipette.pipettes<P>()

    protected inline fun <reified VM : BaseViewModel> fragmentViewModels(
        noinline ownerProducer: () -> ViewModelStoreOwner = { this },
        noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
    ) = viewModels<VM>(ownerProducer, factoryProducer).also { baseViewModel = it }

    companion object {
        const val TAG = "BaseFragment"
    }
}