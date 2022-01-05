package com.keycome.twinkleschedule

import android.content.Intent
import com.keycome.twinkleschedule.activity.SecondActivity
import com.keycome.twinkleschedule.base.BBaseActivity
import kotlinx.coroutines.delay

class MainActivity : BBaseActivity() {

    override suspend fun main() {
        delay(3000)
        startActivity(Intent(this, SecondActivity::class.java))
        finish()
    }
}