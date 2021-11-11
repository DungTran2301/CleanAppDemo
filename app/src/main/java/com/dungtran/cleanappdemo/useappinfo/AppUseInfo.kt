package com.dungtran.cleanappdemo.useappinfo

import android.Manifest
import android.annotation.SuppressLint
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.icu.text.DateFormat.MEDIUM
import androidx.appcompat.app.AppCompatActivity

import android.text.format.DateUtils
import android.util.ArrayMap
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dungtran.cleanappdemo.R
import com.dungtran.cleanappdemo.databinding.ActivityAppUseInfoBinding
import com.dungtran.cleanappdemo.model.AppInfo
import java.text.DateFormat
import java.util.*
import android.app.usage.StorageStats
import android.app.usage.StorageStatsManager
import android.os.*
import android.os.storage.StorageManager
import android.os.storage.StorageManager.UUID_DEFAULT
import android.view.Menu
import android.view.MenuItem
import kotlin.collections.ArrayList


class AppUseInfo : AppCompatActivity() {
    lateinit var binding: ActivityAppUseInfoBinding

    private lateinit var mUsageStatsManager: UsageStatsManager
    private lateinit var mStorageStatsManager: StorageStatsManager

    private lateinit var mPm: PackageManager
    lateinit var adapter: AppInfoAdapter
    private var layoutAppManager: RecyclerView.LayoutManager? = null
    private var mInflater: LayoutInflater? = null

    private val mAppLabelMap = ArrayMap<String, String>()
    private val mAppSizeMap = ArrayMap<String, Long>()


    private val mPackageStats: ArrayList<UsageStats> = ArrayList()
    private val mPackageStorageStats: ArrayList<StorageStats> = ArrayList()

    private var appInfoList: MutableList<AppInfo> = mutableListOf()

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppUseInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mUsageStatsManager = getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager
        mStorageStatsManager = getSystemService(STORAGE_STATS_SERVICE) as StorageStatsManager

        mInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mPm = packageManager
        getInformation()

        convertInformation()

        setUpRecycleView()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onStart() {
        super.onStart()
        binding.toolBar.setOnMenuItemClickListener { it1 ->
            when (it1.itemId) {
                R.id.sort_used_data -> {
                    appInfoList.sortByDescending { it.usedMemory }
                    adapter.notifyDataSetChanged()
                    display()
                    true
                }
                R.id.sort_total_time -> {
                    appInfoList.sortByDescending { it.totalTime }
                    adapter.notifyDataSetChanged()
                    true
                }
                R.id.sort_last_time -> {
                    appInfoList.sortByDescending { it.lastUsed }
                    adapter.notifyDataSetChanged()
                    true
                }
                else -> {
                    print("")
                    true
                }
            }
        }
    }

    private fun setUpRecycleView() {
        adapter = AppInfoAdapter()
        adapter.setData(appInfoList)
        layoutAppManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recycleView.layoutManager = layoutAppManager
        binding.recycleView.adapter = adapter
    }

    private fun display() {
        for (i in 0 until appInfoList.size) {
            val pkgStats = appInfoList[i]
            Log.d("App user information", "stats: " + pkgStats.usedMemory)
        }
    }

    @SuppressLint("NewApi")
    private fun getInformation() {
        val cal: Calendar = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -1)

        val stats: List<UsageStats> = mUsageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_WEEKLY,
            cal.timeInMillis, System.currentTimeMillis()
        )

        val map: ArrayMap<String, UsageStats> = ArrayMap()
        val statCount = stats.size

        for (i in 0 until statCount) {
            val pkgStats = stats[i]

            // load application labels for each application
            try {
                val appInfo: ApplicationInfo = mPm.getApplicationInfo(pkgStats.packageName, 0)
                val label = appInfo.loadLabel(mPm).toString()

                val tmp = mStorageStatsManager.queryStatsForPackage(
                    StorageManager.UUID_DEFAULT, pkgStats.packageName, android.os.Process.myUserHandle())

                mAppLabelMap[pkgStats.packageName] = label
                mAppSizeMap[pkgStats.packageName] = tmp.dataBytes + tmp.appBytes + tmp.cacheBytes
                val existingStats: UsageStats? = map[pkgStats.packageName]
                if (existingStats == null) {
                    map[pkgStats.packageName] = pkgStats
                } else {
                    existingStats.add(pkgStats)
                }
            } catch (e: PackageManager.NameNotFoundException) {
                // This package may be gone.
            }
        }
        mPackageStats.addAll(map.values)
    }


    private fun convertInformation() {
        val appCount = mPackageStats.size
        Log.d("AppUseInfo", "convertInformation: $appCount")
        for (i in 0 until appCount) {
            val name = mAppLabelMap[mPackageStats[i].packageName].toString()
            val usedMemory = mAppSizeMap[mPackageStats[i].packageName]!!.toLong()
            val lastTimeUse = mPackageStats[i].lastTimeUsed
            val usageTime = mPackageStats[i].totalTimeInForeground / 1000
            val icon = mPm.getApplicationIcon(mPackageStats[i].packageName)

            appInfoList.add(AppInfo(name, usedMemory, usageTime, lastTimeUse, icon))
        }

    }
}