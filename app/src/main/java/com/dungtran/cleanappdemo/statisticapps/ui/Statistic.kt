package com.dungtran.cleanappdemo.statisticapps.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dungtran.cleanappdemo.R
import com.dungtran.cleanappdemo.databinding.FragmentStatisticBinding
import com.dungtran.cleanappdemo.statisticapps.viewmodel.GeneralDataFactory
import com.dungtran.cleanappdemo.statisticapps.viewmodel.GeneralViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import android.icu.number.Precision.currency
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.contentValuesOf
import com.dungtran.cleanappdemo.statisticapps.model.AppInfo

import com.github.mikephil.charting.utils.ViewPortHandler
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.Entry

import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.android.material.slider.LabelFormatter
import com.github.mikephil.charting.utils.MPPointF

import com.github.mikephil.charting.components.YAxis.AxisDependency

import com.github.mikephil.charting.data.BarEntry

import android.graphics.RectF





class Statistic : Fragment(), OnChartValueSelectedListener {

    lateinit var binding: FragmentStatisticBinding
    private lateinit var pChart: PieChart
    private lateinit var pChart1: PieChart
    private lateinit var appInfoList: List<AppInfo>

    private val generalViewModel: GeneralViewModel by lazy {
        GeneralDataFactory(requireContext()).create(GeneralViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatisticBinding.inflate(layoutInflater, container, false)


        // Inflate the layout for this fragment
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pChart = view.findViewById(R.id.pie_chart_stats)
        pChart1 = view.findViewById(R.id.pie_chart_stats_1)

        generalViewModel.getApps().observe(viewLifecycleOwner){
            appInfoList = it
            setupPieChart(pChart)
            loadPieChartData(pChart, 1)
            setupPieChart(pChart1)
            loadPieChartData(pChart1, 2)
        }
        pChart.setOnChartValueSelectedListener(this)

    }

    override fun onResume() {
        super.onResume()
        pChart.animateY(1400, Easing.EaseInOutQuad)
        pChart1.animateY(1400, Easing.EaseInOutQuad)
    }

    private fun setupPieChart(pieChart:PieChart) {

        pieChart.description.text = "tran dang dung"
        //hollow pie chart
        pieChart.isDrawHoleEnabled = false
        pieChart.setTouchEnabled(true)
        pieChart.setDrawEntryLabels(false)


        //adding padding
//        pieChart.setExtraOffsets(20f, 20f, 0f, 0f)
//        pieChart.isRotationEnabled = false
//        pieChart.setDrawEntryLabels(false)
//        pieChart.minimumWidth

        //create hole in center
        pieChart.holeRadius = 58f
        pieChart.transparentCircleRadius = 61f
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.WHITE)

        //add text in center
        pieChart.setDrawCenterText(true);
        pieChart.centerText = "App use memory"
        pieChart.setCenterTextSize(18f)
        pieChart.setCenterTextColor(Color.RED)
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)
        pieChart.animateY(1400, Easing.EaseInElastic)//, Easing.EaseInOutQuad)
        pieChart.invalidate()

        val legend: Legend = pieChart.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.isWordWrapEnabled = true
        legend.setDrawInside(false)
        legend.isEnabled = true

    }

    private fun loadPieChartData(pieChart: PieChart, tmp: Int) {
        val dataEntries = ArrayList<PieEntry>()
        appInfoList = appInfoList.sortedByDescending { it.usedMemory }

        val colors: ArrayList<Int> = ArrayList()
        if (tmp == 1) {
            for (i in 0..6) {
                dataEntries.add(PieEntry(appInfoList[i].usedMemory.toFloat(), appInfoList[i].name))
            }
            for (color in ColorTemplate.MATERIAL_COLORS)
                colors.add(color)
            for (color in ColorTemplate.VORDIPLOM_COLORS)
                colors.add(color)
        }
        else {
            for (i in 7 until appInfoList.size) {
                dataEntries.add(PieEntry(appInfoList[i].usedMemory.toFloat(), appInfoList[i].name))
            }
            for (color in ColorTemplate.PASTEL_COLORS)
                colors.add(color)
            for (color in ColorTemplate.JOYFUL_COLORS)
                colors.add(color)
        }


        val dataSet = PieDataSet(dataEntries, "")
        val data = PieData(dataSet)

        // In Percentage


        data.setValueFormatter(LabelFormatter())
        dataSet.sliceSpace = 1f
        dataSet.colors = colors
        pieChart.data = data
        data.setValueTextSize(13f)

//        for (i in 0 until dataSet.)

//        dataSet.xValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE
//        dataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
    }



    class LabelFormatter : IAxisValueFormatter, IValueFormatter {
        override fun getFormattedValue(value: Float, axis: AxisBase): String {
            return convertStorage(value.toLong())
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
                    String.format(if (f > 100) "%.0f MB" else "%.1f MB", f)
                }
                size >= kb -> {
                    val f = size.toFloat() / kb
                    String.format(if (f > 100) "%.0f KB" else "%.1f KB", f)
                }
                else -> String.format("%d B", size)
            }
        }

        override fun getFormattedValue(
            value: Float,
            entry: Entry?,
            dataSetIndex: Int,
            viewPortHandler: ViewPortHandler?
        ): String {
            return convertStorage(value.toLong())
        }
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
//        Log.i("I clicked on", String.valueOf(e.getXIndex()) )

        Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()

    }

    override fun onNothingSelected() {
        Toast.makeText(requireContext(), "NO", Toast.LENGTH_SHORT).show()
    }


}