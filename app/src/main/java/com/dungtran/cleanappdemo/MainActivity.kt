package com.dungtran.cleanappdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.dungtran.cleanappdemo.databinding.ActivityMainBinding
import com.dungtran.cleanappdemo.statisticapps.AppUseInfo
import com.dungtran.cleanappdemo.statisticapps.ui.General

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))


//        binding.btnStartCheck.setOnClickListener {
//            val intent = Intent(this, AppUseInfo::class.java)
//            startActivity(intent)
//        }

        val bundle = Bundle()
        val general = General()

        general.arguments = bundle

        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragments, general).commit()
//        binding.btnStartCheck.setOnClickListener {
//
//            val intent = Intent(this, General::class.java)
//            startActivity(intent)
//        }

    }
}
/*
https://stackoverflow.com/questions/32825854/how-to-get-the-time-spent-on-an-application-in-android-programmatically
https://www.tabnine.com/code/java/methods/android.app.usage.StorageStatsManager/queryStatsForPackage
https://developer.android.com/reference/android/app/usage/StorageStatsManager#queryStatsForPackage(java.util.UUID,%20java.lang.String,%20android.os.UserHandle)
https://developer.android.com/reference/android/app/usage/UsageStats#getLastTimeUsed()

 */