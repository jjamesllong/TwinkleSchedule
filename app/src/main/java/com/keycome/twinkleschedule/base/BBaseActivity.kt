package com.keycome.twinkleschedule.base

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.viewbinding.ViewBinding
import com.keycome.twinkleschedule.extension.BindingProvider
import com.keycome.twinkleschedule.utils.TextColor
import kotlinx.coroutines.*


abstract class BBaseActivity : AppCompatActivity() {

    abstract fun onInit()

    abstract suspend fun onAsync()

    val coroutineScope = MainScope()

    private var design: Design? = null

    var baseViewModel: Lazy<BaseViewModel>? = null

    private var _navHostFragmentId: Int? = null

    private var _navController: NavController? = null
    val navController: NavController
        get() = _navController ?: throw Exception()

    private var defer: (suspend () -> Unit)? = null
    private var deferRunning = false
    protected fun defer(operation: suspend () -> Unit) {
        this.defer = operation
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val windowInsetsController = ViewCompat.getWindowInsetsController(
            findViewById(android.R.id.content)
        )
        windowInsetsController?.isAppearanceLightStatusBars = true
        onInit()
        coroutineScope.launch {
            onAsync()
            finish()
        }
    }

    override fun onDestroy() {
        Log.d("BBaseActivity", "${javaClass.simpleName} onDestroy()")
        coroutineScope.cancel()
        super.onDestroy()
    }

    override fun finish() {
        if (deferRunning) return
        defer?.let { operation ->
            deferRunning = true
            coroutineScope.launch {
                try {
                    operation()
                } finally {
                    withContext(NonCancellable) {
                        super.finish()
                    }
                }
            }
            Unit
        } ?: super.finish()
    }

    override fun onBackPressed() {
        _navController?.run {
            if (!popBackStack()) super.onBackPressed()
        } ?: super.onBackPressed()
    }

    protected inline fun <reified VM : BaseViewModel> activityViewModels(
        noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
    ) = viewModels<VM>(factoryProducer).also { baseViewModel = it }

    protected fun supportNavigation(navHostFragmentId: Int) {
        _navHostFragmentId = navHostFragmentId
    }

    protected fun <VB : ViewBinding, BD : BindingDesign<VB>> supportBindingDesign(
        bindingDesign: BindingDesign<VB>,
        bindingProvider: BindingProvider<VB>
    ) {
        val binding = bindingProvider.doBind()
        setContentView(binding.root)
        _navHostFragmentId?.let { id ->
            val navHostFragment =
                supportFragmentManager.findFragmentById(id) as NavHostFragment
            _navController = navHostFragment.navController
        }
        design = bindingDesign
        design?.performInit(this)
        baseViewModel?.value?.performInit(this)

        bindingDesign.onBind(binding)
    }
}

abstract class BaseActivity2 : AppCompatActivity() {
    fun setStatusBarAppearance(view: View, color: TextColor) {
        val windowInsetsController = ViewCompat.getWindowInsetsController(view)
        windowInsetsController?.isAppearanceLightStatusBars = when (color) {
            TextColor.Light -> false
            TextColor.Dark -> true
        }
    }
}