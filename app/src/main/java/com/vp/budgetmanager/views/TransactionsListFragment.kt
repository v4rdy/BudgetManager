package com.vp.budgetmanager.views

import android.annotation.SuppressLint
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
import com.vp.budgetmanager.utils.INCOME_AMOUNT
import com.vp.budgetmanager.utils.SAVING_AMOUNT
import com.vp.budgetmanager.utils.SharedPref
import com.vp.budgetmanager.viewmodel.TransactionViewModel
import java.math.RoundingMode

class TransactionsListFragment : Fragment() {

    private lateinit var binding: FragmentTransactionsListBinding
    private lateinit var viewModel: TransactionViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransactionsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = RecyclerViewAdapter()
        viewModel = ViewModelProvider(
            requireActivity(),
            (requireActivity().application as TransactionApplication).viewModelFactory
        )[TransactionViewModel::class.java]
        isFirstEnter()
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        var transactions: List<Transaction>

        viewModel.allTransactions.observe(viewLifecycleOwner) { t ->
            t?.let {
                transactions = it
                adapter.submitList(transactions.asReversed())
            }
        }
        setStats()

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.to_new_transaction)
        }

        val liveDataMerger = MediatorLiveData<Int>()
        liveDataMerger.addSource(viewModel.totalSpent) { value ->
            binding.spentAmount.text = "(${value.roundString(2)})"
            val balance = getBalanceAmount(value)

            if (balance > 0) {
                binding.balanceAmount.text = balance.toString()
                binding.savingAmount.text =
                    SharedPref.getInstance(requireContext()).getFloat(SAVING_AMOUNT).roundString(2)
                binding.savingAmount.setTextColor(resources.getColor(R.color.primary_light_green))
            } else {
                binding.balanceAmount.text = "0.0"
                binding.savingAmount.text = (SharedPref.getInstance(requireContext())
                    .getFloat(SAVING_AMOUNT) + balance).roundString(2)
                binding.savingAmount.setTextColor(resources.getColor(R.color.light_red))
            }
        }
        liveDataMerger.observe(viewLifecycleOwner) {}
    }

    private fun Float.roundString(scale: Int): String {
        return this.toBigDecimal().setScale(scale, RoundingMode.HALF_EVEN).toString()
    }

    private fun setStats() {
        binding.incomeAmount.text =
            SharedPref.getInstance(requireContext()).getFloat(INCOME_AMOUNT).toString()
        binding.savingAmount.text =
            SharedPref.getInstance(requireContext()).getFloat(SAVING_AMOUNT).toString()
    }

    private fun isFirstEnter() {
        if (viewModel.isFirstEnter(requireContext()))
            findNavController().navigate(R.id.to_first_enter)
    }

    private fun getBalanceAmount(total: Float): Float {
        return SharedPref.getInstance(requireContext())
            .getFloat(INCOME_AMOUNT) - total - SharedPref.getInstance(requireContext())
            .getFloat(SAVING_AMOUNT)
    }
}