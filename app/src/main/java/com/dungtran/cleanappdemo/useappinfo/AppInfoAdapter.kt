package com.dungtran.cleanappdemo.useappinfo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dungtran.cleanappdemo.R
import com.dungtran.cleanappdemo.model.AppInfo
import java.text.DateFormat
import android.icu.text.DateFormat.MEDIUM
import android.app.usage.UsageStats

import android.content.pm.ApplicationInfo

import android.app.usage.UsageStatsManager
import android.content.pm.PackageManager
import android.icu.text.DateFormat.MEDIUM
import android.text.format.DateUtils
import android.util.ArrayMap
import java.text.DateFormat.MEDIUM
import java.util.*
import kotlin.collections.ArrayList


class AppInfoAdapter: RecyclerView.Adapter<AppInfoAdapter.ViewHolder>() {

    private var appInfoList: List<AppInfo> = listOf()


    fun setData(dishList: List<AppInfo>) {
        this.appInfoList = dishList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppInfoAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.app_card_info, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: AppInfoAdapter.ViewHolder, position: Int) {
        holder.initialize(appInfoList[position])
    }

    override fun getItemCount(): Int {
        return appInfoList.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var appName: TextView = itemView.findViewById(R.id.tv_app_name)
        var usedMemory: TextView = itemView.findViewById(R.id.tv_used_memory)
        var usedTime: TextView = itemView.findViewById(R.id.tv_used_time)
        var lastUse: TextView = itemView.findViewById(R.id.tv_last_use)

        fun initialize(item: AppInfo) {
            appName.text = item.name
            usedMemory.text = convertStorage(item.usedMemory)
            usedTime.text = DateUtils.formatElapsedTime(
                item.totalTime / 1000)
            lastUse.text = DateUtils.formatSameDayTime(item.lastUsed,
                System.currentTimeMillis(), DateFormat.MEDIUM, DateFormat.MEDIUM)

        }
    }

    private fun convertStorage(size: Long): String {
        val kb: Long = 1024
        val mb = kb * 1024
        val gb = mb * 1024
        return when {
            size >= gb -> {
                String.format("%.1f GB", size.toFloat() / gb)
            }
            size >= mb -> {
                val f = size.toFloat() / mb
                String.format(if (f > 100) "%.0f MB" else "%.1f MB", f)
            }
            size >= kb -> {
                val f = size.toFloat() / kb
                String.format(if (f > 100) "%.0f KB" else "%.1f KB", f)
            }
            else -> String.format("%d B", size)
        }
    }

}