package com.keycome.twinkleschedule

import android.content.Intent
import android.os.Bundle
import com.keycome.twinkleschedule.activity.SecondActivity
import com.keycome.twinkleschedule.base.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, SecondActivity::class.java))
        finish()
    }
}