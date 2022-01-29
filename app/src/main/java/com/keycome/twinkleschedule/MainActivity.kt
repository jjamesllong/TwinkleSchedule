package com.keycome.twinkleschedule

import android.content.Intent
import com.keycome.twinkleschedule.activity.SecondActivity
import com.keycome.twinkleschedule.base.BBaseActivity

class MainActivity : BBaseActivity() {

    override suspend fun main() {
        startActivity(Intent(this, SecondActivity::class.java))
        finish()
    }
}