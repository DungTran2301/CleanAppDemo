package com.dungtran.cleanappdemo.statisticapps.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dungtran.cleanappdemo.databinding.FragmentGeneralBinding
import com.dungtran.cleanappdemo.statisticapps.adapter.StatisticPageAdapter
import com.google.android.material.tabs.TabLayoutMediator

class General : Fragment() {
    lateinit var binding: FragmentGeneralBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGeneralBinding.inflate(inflater, container, false)

        val adapter = StatisticPageAdapter(childFragmentManager, lifecycle)
        binding.vp2ContainFragment.adapter = adapter

        TabLayoutMediator(binding.tabLayoutTarget, binding.vp2ContainFragment) { tab, position ->
            when(position) {
                0->{
                    tab.text = "All app"
                }
                1->{
                    tab.text = "Chart"
                }
            }
        }.attach()

        return binding.root
    }
}