package com.keycome.twinkleschedule.base

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import kotlinx.coroutines.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

abstract class BBaseActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    protected abstract suspend fun main()

    private var design: Design<*>? = null
        set(parameter) {
            field = parameter
            setContentView(parameter?.binding?.root ?: View(this))
        }

    private var _navController: NavController? = null
    protected val navController: NavController
        get() = _navController ?: throw Exception()

    private var defer: (suspend () -> Unit)? = null
    private var deferRunning = false
    protected fun defer(operation: suspend () -> Unit) {
        this.defer = operation
    }

    private var _key: String? = null
    protected fun supportKey(key: String) {
        _key = key
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        launch {
            main()
            finish()
        }
    }

    override fun onDestroy() {
        Log.d("BBaseActivity", "onDestroy")
        design?.cancel()
        this.cancel()
        Log.d("BBaseActivity", javaClass.simpleName)
        Pipette.pipettes.shareTarget.forEach {
            Log.d("BBaseActivity", "before release " + "${it.value}")
        }
        releasePipettes(_key ?: "")
        Pipette.pipettes.shareTarget.forEach {
            Log.d("BBaseActivity", "after release " + "${it.value}")
        }
        super.onDestroy()
    }

    override fun finish() {

        if (deferRunning) return

        deferRunning = true

        launch {
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

    private fun releasePipettes(key: String) = Pipette.releasePipettes(key)

    protected suspend fun setContentDesign(design: Design<*>) {
        suspendCoroutine<Unit> {
            window.decorView.post {
                this.design = design
                it.resume(Unit)
            }
        }
    }

    protected fun supportNavigation(navHostFragmentId: Int): NavController {
        val navHostFragment =
            supportFragmentManager.findFragmentById(navHostFragmentId) as NavHostFragment
        return navHostFragment.navController.also { _navController = it }
    }
}