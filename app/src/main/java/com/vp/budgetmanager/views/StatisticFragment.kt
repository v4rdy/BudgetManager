package com.vp.budgetmanager.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.anychart.AnyChart
import com.anychart.charts.Pie
import com.vp.budgetmanager.R
import com.vp.budgetmanager.databinding.FragmentStatisticBinding
import com.vp.budgetmanager.viewmodel.TransactionViewModel


class StatisticFragment : Fragment() {

    private lateinit var binding: FragmentStatisticBinding
    private lateinit var viewModel: TransactionViewModel
    private  lateinit var chart: Pie

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatisticBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        setListeners()
        drawChart()
    }

    private fun initialize(){
        viewModel = ViewModelProvider(requireActivity())[TransactionViewModel::class.java]
        chart = AnyChart.pie()
    }

    private fun setListeners(){
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun drawChart(){
        val categories = resources.getStringArray(R.array.categories).toList()
        val data = viewModel.getDataListForChart(categories)
        if(data.isEmpty()){
            showMessage()
        }else
        chart.data(data)
        binding.chart.setChart(chart)
    }

    private fun showMessage(){
        binding.chart.visibility = View.GONE
        binding.noStatistic.visibility = View.VISIBLE
    }
}