package com.keycome.twinkleschedule

import android.content.Intent
import com.keycome.twinkleschedule.activity.SecondActivity
import com.keycome.twinkleschedule.base.BBaseActivity
import kotlinx.coroutines.awaitCancellation

class MainActivity : BBaseActivity() {

    override fun onInit() {
        startActivity(Intent(this, SecondActivity::class.java))
        finish()
    }

    override suspend fun onAsync() {
        awaitCancellation()
    }
}