package com.dungtran.cleanappdemo.statisticapps.viewmodel

import android.annotation.SuppressLint
import android.app.usage.StorageStats
import android.app.usage.StorageStatsManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.storage.StorageManager
import android.util.ArrayMap
import android.util.Log
import android.view.LayoutInflater
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.dungtran.cleanappdemo.statisticapps.adapter.AppInfoAdapter
import com.dungtran.cleanappdemo.statisticapps.model.AppInfo
import java.util.*
import kotlin.collections.ArrayList

@RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
class GeneralViewModel(private var context: Context) : ViewModel(){

    private lateinit var mUsageStatsManager: UsageStatsManager
    private lateinit var mStorageStatsManager: StorageStatsManager

    private lateinit var mPm: PackageManager

    private val mAppLabelMap = ArrayMap<String, String>()
    private val mAppSizeMap = ArrayMap<String, Long>()


    private val mPackageStats: ArrayList<UsageStats> = ArrayList()
    private val mPackageStorageStats: ArrayList<StorageStats> = ArrayList()
    private var mInflater: LayoutInflater? = null
    private val appInfoList = MutableLiveData<List<AppInfo>> ()

//    var appInfoList: LiveData<List<AppInfo>> = _appInfoList

    var list: MutableList<AppInfo> = mutableListOf()

//    private var _appInfoList: MutableList<AppInfo> = mutableListOf()
//    var appInfoList: MutableList<AppInfo> = mutableListOf()

    init {
        getInformation()
        appInfoList.postValue(list)
    }

    fun getApps(): LiveData<List<AppInfo>> {
        return appInfoList
    }


    @SuppressLint("NewApi")
    private fun getInformation() {
        mUsageStatsManager = context.getSystemService(AppCompatActivity.USAGE_STATS_SERVICE) as UsageStatsManager
        mStorageStatsManager = context.getSystemService(AppCompatActivity.STORAGE_STATS_SERVICE) as StorageStatsManager
        mInflater = context.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mPm = context.packageManager
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
                mAppSizeMap[pkgStats.packageName] = tmp.dataBytes + tmp.appBytes + tmp.cacheBytes //+ tmp.externalCacheBytes
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
        convertInformation()
    }


    @SuppressLint("WrongConstant")
    private fun convertInformation() {
        val appCount = mPackageStats.size
        Log.d("AppUseInfo", "convertInformation: $appCount")
        for (i in 0 until appCount) {
            val name = mAppLabelMap[mPackageStats[i].packageName].toString()
            val usedMemory = mAppSizeMap[mPackageStats[i].packageName]!!.toLong()
            val lastTimeUse = mPackageStats[i].lastTimeUsed
            val usageTime = mPackageStats[i].totalTimeInForeground / 1000
            val icon = mPm.getApplicationIcon(mPackageStats[i].packageName)

            val m = mPm.getApplicationInfo(mPackageStats[i].packageName, PackageManager.GET_META_DATA)
            val isSystemApp = m.flags and ApplicationInfo.FLAG_SYSTEM
            if (isSystemApp == 0 || name == "Facebook") {
                list.add(AppInfo(name, usedMemory, usageTime, lastTimeUse, icon))
            }
        }

    }


    companion object{
        @SuppressLint("StaticFieldLeak")
        private var instance: GeneralViewModel? = null
        fun getInstance(context: Context): GeneralViewModel{
            if(instance == null){
                instance = GeneralViewModel(context)
            }
            return instance!!
        }
    }
}

class GeneralDataFactory(private val context: Context) : ViewModelProvider.Factory {
//    private val context = context
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GeneralViewModel.getInstance(context) as T
    }
}