package com.dungtran.cleanappdemo.statisticapps.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dungtran.cleanappdemo.R
import com.dungtran.cleanappdemo.statisticapps.model.AppInfo
import java.text.DateFormat

import android.text.format.DateUtils
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import kotlin.math.max


class AppInfoAdapter: RecyclerView.Adapter<AppInfoAdapter.ViewHolder>() {

    private var appInfoList: List<AppInfo> = listOf()
    private var maxUseMemory: Long = 0


    fun setData(appList: List<AppInfo>) {
        this.appInfoList = appList
        for (item in appList) {
            val tmp = item.usedMemory
            if (tmp > maxUseMemory)
                maxUseMemory = tmp
        }
        maxUseMemory = (maxUseMemory*1.5).toLong()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.app_card_info, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
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
        var iconApp: ImageView = itemView.findViewById(R.id.img_icon_app)
        var progressBar: ProgressBar = itemView.findViewById(R.id.pb_memory_percent)

        fun initialize(item: AppInfo) {
            appName.text = item.name
            usedMemory.text = convertStorage(item.usedMemory)
            usedTime.text = DateUtils.formatElapsedTime(
                item.totalTime / 1000)
            lastUse.text = DateUtils.formatSameDayTime(item.lastUsed,
                System.currentTimeMillis(), DateFormat.MEDIUM, DateFormat.MEDIUM)
            iconApp.setImageDrawable(item.icon)
            progressBar.progress = (item.usedMemory*100/maxUseMemory).toInt()

        }
    }

    private fun convertStorage(size: Long): String {
        val kb: Long = 1024
        val mb = kb * 1024
        val gb = mb * 1024
        return when {
            size >= gb -> {
                String.format("%.2f GB", size.toFloat() / gb)
            }
            size >= mb -> {
                val f = size.toFloat() / mb
                String.format(if (f > 100) "%.1f MB" else "%.0f MB", f)
            }
            size >= kb -> {
                val f = size.toFloat() / kb
                String.format(if (f > 100) "%.0f KB" else "%.0f KB", f)
            }
            else -> String.format("%d B", size)
        }
    }

}