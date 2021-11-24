package com.dungtran.cleanappdemo.statisticapps.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dungtran.cleanappdemo.statisticapps.ui.ListApp
import com.dungtran.cleanappdemo.statisticapps.ui.Statistic

class StatisticPageAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0->{
                ListApp()
            }
            1->{
                Statistic()
            }
            else->{
                ListApp()
            }

        }
    }
}