package com.keycome.twinkleschedule.base

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.keycome.twinkleschedule.App
import kotlinx.coroutines.*

abstract class BBaseActivity : AppCompatActivity() {

    protected abstract suspend fun main()

    val coroutineScope = MainScope()

    private var designInitializing = false

    private var design: Design? = null
        set(parameter) {
            field = parameter
            setContentView(parameter?.rootView ?: View(this))
        }
        get() = if (designInitializing) null else field

    private var _navHostFragmentId: Int? = null

    private var _navController: NavController? = null
    protected val navController: NavController
        get() = _navController ?: throw Exception()

    private var defer: (suspend () -> Unit)? = null
    private var deferRunning = false
    protected fun defer(operation: suspend () -> Unit) {
        this.defer = operation
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        coroutineScope.launch {
            main()
            finish()
        }
    }

    override fun onDestroy() {
        Log.d("BBaseActivity", "${javaClass.simpleName} onDestroy")
        design?.coroutineScope?.cancel()
        coroutineScope.cancel()
        super.onDestroy()
    }

    override fun finish() {

        if (deferRunning) return

        deferRunning = true

        coroutineScope.launch {
            try {
                defer?.invoke()
            } finally {
                withContext(NonCancellable) {
                    super.finish()
                }
            }
        }
    }

    override fun onBackPressed() {
        _navController?.run {
            if (!popBackStack()) super.onBackPressed()
        } ?: super.onBackPressed()
    }

    protected inline fun <reified P : Pipette> pipettes() = Pipette.pipettes<P>()

    protected fun supportNavigation(navHostFragmentId: Int) {
        _navHostFragmentId = navHostFragmentId
        design?.let {
            val navHostFragment =
                supportFragmentManager.findFragmentById(navHostFragmentId) as NavHostFragment
            _navController = navHostFragment.navController
        }
    }

    protected fun supportDesign(design: Design) {
        coroutineScope.launch {
            delay(App.designDelay)
            withContext(NonCancellable) {
                designInitializing = true
                this@BBaseActivity.design = design
            }
            designInitializing = false
            _navHostFragmentId?.let { id ->
                val navHostFragment =
                    supportFragmentManager.findFragmentById(id) as NavHostFragment
                _navController = navHostFragment.navController
            }
        }
    }
}