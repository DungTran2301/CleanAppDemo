package com.dungtran.cleanappdemo.useappinfo

import android.Manifest
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.icu.text.DateFormat.MEDIUM
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

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
import android.app.AppOpsManager
import android.os.Process
import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.os.Debug


class AppUseInfo : AppCompatActivity() {
    lateinit var binding: ActivityAppUseInfoBinding

    private lateinit var mUsageStatsManager: UsageStatsManager
    private lateinit var mPm: PackageManager
    lateinit var adapter: AppInfoAdapter
    lateinit var recyclerViewApp: RecyclerView
    private var layoutAppManager: RecyclerView.LayoutManager? = null
    private var mInflater: LayoutInflater? = null

    private val mAppLabelMap = ArrayMap<String, String>()
    private val mPackageStats: ArrayList<UsageStats> = ArrayList()

    private var appInfoList: ArrayList<AppInfo> = ArrayList()


    fun getInformation() {
        val cal: Calendar = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -5)

        val stats: List<UsageStats> = mUsageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_BEST,
            cal.timeInMillis, System.currentTimeMillis()
        )



        val map: ArrayMap<String, UsageStats> = ArrayMap()
        val statCount = stats.size

        Log.d("AppUseInfo", "getInformation: $statCount")

        for (i in 0 until statCount) {
            val pkgStats = stats[i]

            // load application labels for each application
            try {
                val appInfo: ApplicationInfo = mPm.getApplicationInfo(pkgStats.packageName, 0)
                val label = appInfo.loadLabel(mPm).toString()
                mAppLabelMap[pkgStats.packageName] = label
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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppUseInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

//
//        val am = applicationContext.getSystemService(ACTIVITY_SERVICE)
//        val list = am.runningAppProcesses
//        if (list != null) {
//            for (i in list.indices) {
//                val appinfo = list[i]
//                val myMempid = intArrayOf(appinfo.pid)
//                val appMem: Array<Debug.MemoryInfo> = am.getProcessMemoryInfo(myMempid)
//                val memSize: Int = appMem[0].dalvikPrivateDirty / 1024
//                Log.e("AppMemory", appinfo.processName + ":" + memSize)
//            }
//        }


        mUsageStatsManager = getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager
        mInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mPm = packageManager
        getInformation()

        adapter = AppInfoAdapter()
        convertInformation()
        adapter.setData(appInfoList)
        layoutAppManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recycleView.layoutManager = layoutAppManager

        binding.recycleView.adapter = adapter
    }


    private fun convertInformation() {
        val appCount = mPackageStats.size
        Log.d("AppUseInfo", "convertInformation: $appCount")
//        val tmp = AppInfo(name, 40, usageTime, lastTimeUse)
        for (i in 0 until appCount) {
            val name = mAppLabelMap[mPackageStats[i].packageName].toString()
            val lastTimeUse = DateUtils.formatSameDayTime(mPackageStats[i].lastTimeUsed,
                System.currentTimeMillis(), DateFormat.MEDIUM, DateFormat.MEDIUM).toString()
            val usageTime = DateUtils.formatElapsedTime(
                mPackageStats[i].totalTimeInForeground / 1000).toString()
            if (lastTimeUse != "1 Th1, 1970") {

                val tmp = AppInfo(name, 40, usageTime, lastTimeUse)
                appInfoList.add(tmp)
            }
        }

    }


}