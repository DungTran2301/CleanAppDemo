package com.dungtran.cleanappdemo.statisticapps.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dungtran.cleanappdemo.R
import com.dungtran.cleanappdemo.databinding.FragmentListAppBinding
import com.dungtran.cleanappdemo.statisticapps.adapter.AppInfoAdapter
import com.dungtran.cleanappdemo.statisticapps.model.AppInfo
import com.dungtran.cleanappdemo.statisticapps.viewmodel.GeneralDataFactory
import com.dungtran.cleanappdemo.statisticapps.viewmodel.GeneralViewModel

class ListApp : Fragment() {

    lateinit var binding: FragmentListAppBinding

    private val generalViewModel: GeneralViewModel by lazy {
        GeneralDataFactory(requireContext()).create(GeneralViewModel::class.java)
    }

    lateinit var adapter: AppInfoAdapter
    private var layoutAppManager: RecyclerView.LayoutManager? = null
    private var mInflater: LayoutInflater? = null

//    lateinit var appInfoList: List<AppInfo>

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListAppBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mInflater = requireActivity().getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        adapter = AppInfoAdapter()

        generalViewModel.getApps().observe(viewLifecycleOwner) {
            //appInfoList = it
            Log.d("LítApp", "onCreateView: ${it.size}")
            adapter.setData(it)
        }
        setUpRecycleView()

    }

    private fun setUpRecycleView() {
//        adapter = AppInfoAdapter()
//        adapter.setData(appInfoList)
        layoutAppManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recycleView.layoutManager = layoutAppManager
        binding.recycleView.adapter = adapter
    }

//
//        private fun setupPieChart() {
//        binding.pieChartStats.isDrawHoleEnabled = true
//        binding.pieChartStats.setUsePercentValues(true)
//        binding.pieChartStats.setEntryLabelTextSize(12F)
//        binding.pieChartStats.setEntryLabelColor(Color.BLACK)
//        binding.pieChartStats.centerText = "Spending by Category"
//        binding.pieChartStats.setCenterTextSize(24F)
//        binding.pieChartStats.description.isEnabled = false
//        val l: Legend = binding.pieChartStats.legend
//        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
//        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
//        l.orientation = Legend.LegendOrientation.VERTICAL
//        l.setDrawInside(false)
//        l.isEnabled = true
//    }
//
//    private fun loadPieChartData() {
//        val entries: ArrayList<PieEntry> = ArrayList()
//        entries.add(PieEntry(0.2f, "Food & Dining"))
//        entries.add(PieEntry(0.15f, "Medical"))
//        entries.add(PieEntry(0.10f, "Entertainment"))
//        entries.add(PieEntry(0.25f, "Electricity and Gas"))
//        entries.add(PieEntry(0.3f, "Housing"))
//        val colors: ArrayList<Int> = ArrayList()
//        for (color in ColorTemplate.MATERIAL_COLORS) {
//            colors.add(color)
//        }
//        for (color in ColorTemplate.VORDIPLOM_COLORS) {
//            colors.add(color)
//        }


//        val dataSet = PieDataSet(entries, "Expense Category")
//        dataSet.colors = colors
//        val data = PieData(dataSet)
//        data.setDrawValues(true)
//
//        data.setValueFormatter(PercentFormatter())
//        data.setValueTextSize(12f)
//        data.setValueTextColor(Color.BLACK)
//        binding.pieChartStats.data = data
//        binding.pieChartStats.invalidate()
//        binding.pieChartStats.animateY(1400, Easing.EaseInOutQuad)
//    }

}