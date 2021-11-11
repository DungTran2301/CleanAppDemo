package com.dungtran.cleanappdemo.useappinfo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dungtran.cleanappdemo.R
import com.dungtran.cleanappdemo.model.AppInfo
import android.app.usage.UsageStats

import android.content.pm.ApplicationInfo

import android.app.usage.UsageStatsManager
import android.content.pm.PackageManager
import android.util.ArrayMap
import java.util.*
import kotlin.collections.ArrayList


class AppInfoAdapter: RecyclerView.Adapter<AppInfoAdapter.ViewHolder>() {

    private var appInfoList: ArrayList<AppInfo> = ArrayList()


    fun setData(dishList: ArrayList<AppInfo>) {
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

//        init {
//            dishImage = itemView.findViewById(R.id.img_dish_home_main)
//            dishName = itemView.findViewById(R.id.tv_dish_name_home_main)
//        }

        fun initialize(item: AppInfo) {
            appName.text = item.name
            usedMemory.text = item.usedMemory.toString()
            usedTime.text = item.totalTime.toString()
            lastUse.text = item.lastUsed

        }
    }



}