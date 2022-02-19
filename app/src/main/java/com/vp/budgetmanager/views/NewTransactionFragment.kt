package com.vp.budgetmanager.views

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.vp.budgetmanager.R
import com.vp.budgetmanager.databinding.FragmentNewTransactionBinding
import com.vp.budgetmanager.model.Transaction
import com.vp.budgetmanager.viewmodel.TransactionViewModel

class NewTransactionFragment : Fragment() {

    private lateinit var viewModel: TransactionViewModel
    private var type: String = ""
    private var period: String = ""

    private lateinit var binding: FragmentNewTransactionBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewTransactionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[TransactionViewModel::class.java]
        initialize()
        setListeners()
    }

    private fun initialize() {
        ArrayAdapter
            .createFromResource(
                requireContext(),
                R.array.categories,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
            )
            .also { adapter ->
                adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
                binding.categoryMenu.adapter = adapter
            }
        binding.categoryMenu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                type = parent?.getItemAtPosition(position) as String
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.period_options,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
            binding.periodMenu.adapter = adapter
        }

        binding.periodMenu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                period = parent?.getItemAtPosition(position) as String
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun setListeners() {
        binding.addButton.setOnClickListener {
            when {
                TextUtils.isEmpty(binding.amountInput.text) -> {
                    binding.amountInput.error = "Wymagane pole"
                }
                binding.amountInput.text.toString().toFloat() > Float.MAX_VALUE -> {
                    binding.amountInput.error = "Niepoprawny format danych"
                    Snackbar.make(it, "Niepoprawny format danych", Snackbar.LENGTH_LONG).show()
                }
                else -> {
                    addTransactionAndClose()
                }
            }
        }
    }

    private fun addTransactionAndClose() {
        val date = System.currentTimeMillis()
        val transaction =
            Transaction(
                amount = binding.amountInput.text.toString().toFloat(),
                type = type,
                name = binding.nameInput.text.toString(),
                period = period,
                time = date
            )
        viewModel.insert(transaction)
        Snackbar.make(binding.root, "Koszt został dodany", Snackbar.LENGTH_LONG).show()
        findNavController().popBackStack()
    }

}