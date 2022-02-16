package com.vp.budgetmanager.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MediatorLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.vp.budgetmanager.NewTransactionActivity
import com.vp.budgetmanager.RecyclerViewAdapter
import com.vp.budgetmanager.TransactionApplication
import com.vp.budgetmanager.databinding.FragmentTransactionsListBinding
import com.vp.budgetmanager.model.Transaction
import com.vp.budgetmanager.viewmodel.TransactionViewModel
import com.vp.budgetmanager.viewmodel.TransactionViewModelFactory
import java.util.*

class TransactionsListFragment : Fragment() {

    private val transactionViewModel : TransactionViewModel by viewModels {
        TransactionViewModelFactory((requireActivity().application as TransactionApplication).repository)
    }
    private lateinit var binding: FragmentTransactionsListBinding
    private val newTransactionActivityRequestCode = 1

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
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

        var transactions: List<Transaction>

        transactionViewModel.allTransactions.observe(viewLifecycleOwner) { t ->
            t?.let {
                transactions = it
                adapter.submitList(transactions.asReversed())
            }
        }


        binding.fab.setOnClickListener{
        }

        val cal = Calendar.getInstance().timeInMillis

        var total = transactionViewModel.totalSpent.value
        var today = transactionViewModel.getTodaySpent(cal).value

        val liveDataMerger = MediatorLiveData<Int>()
        liveDataMerger.addSource(transactionViewModel.totalSpent){ value ->
            total = value
            combineValues(total,today)
            Toast.makeText(requireContext(),"TT", Toast.LENGTH_SHORT).show()
        }
        liveDataMerger.addSource(transactionViewModel.getTodaySpent(cal)){ value ->
            today = value
            combineValues(total,today)
            Toast.makeText(requireContext(),"TO", Toast.LENGTH_SHORT).show()
        }
        liveDataMerger.observe(viewLifecycleOwner) {}
    }


    private fun combineValues(total: Int?, today: Int?){
        if(total!=null  &&  today!=null) {
            val s = "Total Spent: $total\nToday's Spent: $today";
            binding.overview.text = s
        }
    }
}