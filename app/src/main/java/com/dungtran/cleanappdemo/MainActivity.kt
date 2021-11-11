package com.dungtran.cleanappdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import com.dungtran.cleanappdemo.databinding.ActivityMainBinding
import com.dungtran.cleanappdemo.useappinfo.AppUseInfo

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        binding.btnStartCheck.setOnClickListener {
            val intent = Intent(this, AppUseInfo::class.java)
            startActivity(intent)
        }
    }
}