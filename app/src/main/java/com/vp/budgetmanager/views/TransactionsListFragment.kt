package com.vp.budgetmanager.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.vp.budgetmanager.R
import com.vp.budgetmanager.views.utils.RecyclerViewAdapter
import com.vp.budgetmanager.TransactionApplication
import com.vp.budgetmanager.databinding.FragmentTransactionsListBinding
import com.vp.budgetmanager.model.Transaction
import com.vp.budgetmanager.viewmodel.TransactionViewModel
import java.util.*

class TransactionsListFragment : Fragment() {

    private lateinit var binding: FragmentTransactionsListBinding
    private lateinit var viewModel: TransactionViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransactionsListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = RecyclerViewAdapter()
        viewModel = ViewModelProvider(requireActivity(), (requireActivity().application as TransactionApplication).viewModelFactory)[TransactionViewModel::class.java]

        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

        var transactions: List<Transaction>

        viewModel.allTransactions.observe(viewLifecycleOwner) { t ->
            t?.let {
                transactions = it
                adapter.submitList(transactions.asReversed())
            }
        }


        binding.fab.setOnClickListener{
            findNavController().navigate(R.id.to_new_transaction)
        }

        val cal = Calendar.getInstance().timeInMillis

        var total = viewModel.totalSpent.value
        var today = viewModel.getTodaySpent(cal).value

        val liveDataMerger = MediatorLiveData<Int>()
        liveDataMerger.addSource(viewModel.totalSpent){ value ->
            total = value
            combineValues(total,today)
        }
        liveDataMerger.addSource(viewModel.getTodaySpent(cal)){ value ->
            today = value
            combineValues(total,today)
        }
        liveDataMerger.observe(viewLifecycleOwner) {}
    }


    private fun combineValues(total: Int?, today: Int?){
        if(total!=null  &&  today!=null) {
            val s = "Total Spent: $total\nToday's Spent: $today"
            binding.overview.text = s
        }
    }
}