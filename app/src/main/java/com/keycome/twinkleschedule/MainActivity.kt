package com.keycome.twinkleschedule

import android.content.Intent
import android.os.Bundle
import com.keycome.twinkleschedule.databinding.ActivityMainBinding
import com.keycome.twinkleschedule.presentation.display.DisplayActivity

class MainActivity : BaseActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startActivity(Intent(this, DisplayActivity::class.java))
    }
}