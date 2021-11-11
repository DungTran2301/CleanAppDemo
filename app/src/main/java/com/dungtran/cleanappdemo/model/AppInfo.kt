package com.dungtran.cleanappdemo.model

import android.graphics.drawable.Drawable

data class AppInfo(
    var name: String,
    var usedMemory: Long,
    var totalTime: Long,
    var lastUsed: Long,
    var icon: Drawable) {

}