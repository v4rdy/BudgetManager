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

class TransactionsListFragment : Fragment() {

    private lateinit var binding: FragmentTransactionsListBinding
    private lateinit var viewModel: TransactionViewModel
    private  var savingAmount = 0.0F
    private var incomeAmount = 0.0F
    private var adapter = RecyclerViewAdapter()
    private lateinit var transactions: List<Transaction>

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
        initialize()
        checkIsFirstEnter()
        setListeners()
        setObservers()
    }

    private fun setListeners(){
        binding.statCard.setOnClickListener {
            findNavController().navigate(R.id.statistic_fragment)
        }
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.to_new_transaction)
        }
    }

    private fun initialize(){
        viewModel = ViewModelProvider(
            requireActivity(),
            (requireActivity().application as TransactionApplication).viewModelFactory
        )[TransactionViewModel::class.java]

        savingAmount = SharedPref.getInstance(requireContext()).getFloat(SAVING_AMOUNT)
        incomeAmount = SharedPref.getInstance(requireContext()).getFloat(INCOME_AMOUNT)
        setStats()
    }

    private fun setObservers(){
        viewModel.allTransactions.observe(viewLifecycleOwner) { t ->
            t?.let {
                transactions = it
                adapter.submitList(transactions.asReversed())
                setListVisibility(transactions.isNotEmpty())
            }
        }
    }

    private fun setListVisibility(isTransactions: Boolean){
        if(isTransactions){
            binding.noTransactions.visibility = View.GONE
            binding.recyclerview.visibility = View.VISIBLE
            changeAmounts()
        }else {
            binding.noTransactions.visibility = View.VISIBLE
            binding.recyclerview.visibility = View.GONE
        }
    }

    private fun changeAmounts(){
        val liveDataMerger = MediatorLiveData<Int>()
        liveDataMerger.addSource(viewModel.totalSpent) { value ->
            var total = 0.00f
            if(value != null) total = value
            binding.spentAmount.text = getString(R.string.value_in_brackets, total.round(2))
            val balance = getBalanceAmount(total)
            if (balance > 0) {
                binding.messageLayout.visibility = View.GONE
                binding.balanceAmount.text = getString(R.string.currency_value, balance.round(2))
                binding.balancePercent.text = viewModel.calculateBalancePercent(balance, incomeAmount, savingAmount)
                binding.savingAmount.text = getString(R.string.currency_value, savingAmount.round(2))
                binding.savingAmount.setTextColor(resources.getColor(R.color.primary_light_green))
            } else {
                binding.balancePercent.text = getString(R.string.zero_percentage)
                binding.balanceAmount.text = getString(R.string.currency_value, "0.00")
                binding.messageLayout.visibility = View.VISIBLE
                binding.budgetMessage.text = getString(R.string.budget_exceeded_message, viewModel.calculateBudgetExceededPercent(total, incomeAmount, savingAmount).round(1))
                binding.savingAmount.text = getString(R.string.value, (savingAmount + balance).round(2))
                binding.savingAmount.setTextColor(resources.getColor(R.color.light_red))
            }
        }
        liveDataMerger.observe(viewLifecycleOwner) {}
    }

    private fun Float.round(scale: Int): String {
        return "%.${scale}f".format(this)
    }

    private fun setStats() {
        binding.incomeAmount.text = getString(R.string.currency_value, incomeAmount.round(2))
        binding.savingAmount.text =getString(R.string.currency_value, savingAmount.round(2))
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun checkIsFirstEnter() {
        if (viewModel.isFirstEnter(requireContext()))
            findNavController().navigate(R.id.to_first_enter)
    }

    private fun getBalanceAmount(total: Float): Float {
        return SharedPref.getInstance(requireContext())
            .getFloat(INCOME_AMOUNT) - total - SharedPref.getInstance(requireContext())
            .getFloat(SAVING_AMOUNT)
    }
}