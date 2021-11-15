package com.dungtran.cleanappdemo.statisticapps.model

import android.graphics.drawable.Drawable

data class AppInfo(
    var name: String,
    var usedMemory: Long,
    var totalTime: Long,
    var lastUsed: Long,
    var icon: Drawable) {

}