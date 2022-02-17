package com.vp.budgetmanager.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.vp.budgetmanager.R
import com.vp.budgetmanager.databinding.FragmentFirstEnterBinding
import com.vp.budgetmanager.utils.INCOME_AMOUNT
import com.vp.budgetmanager.utils.NAME
import com.vp.budgetmanager.utils.SAVING_AMOUNT
import com.vp.budgetmanager.utils.SharedPref

class FirstEnterFragment : Fragment() {

    private lateinit var binding: FragmentFirstEnterBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFirstEnterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners(){
        binding.enterButton.setOnClickListener {
            if (!isErrors()) {
                saveData()
                findNavController().popBackStack()
            }
        }
    }

    private fun saveData() {
        SharedPref.getInstance(requireContext())
            .saveString(binding.nameInput.text.toString(), NAME)
        SharedPref.getInstance(requireContext())
            .saveFloat(binding.incomeInput.text.toString().toFloat(), INCOME_AMOUNT)
        SharedPref.getInstance(requireContext())
            .saveFloat(binding.savingInput.text.toString().toFloat(), SAVING_AMOUNT)
    }

    private fun isErrors(): Boolean {
        var isErrors = false
        if (binding.nameInput.text.toString().isEmpty()) {
            binding.nameInput.error = "Pole jest puste"
            isErrors = true
        }
        if (binding.incomeInput.text.toString().isEmpty()) {
            binding.incomeInput.error = "Pole jest puste"
            isErrors = true
        }
        if (binding.savingInput.text.toString().isEmpty()) {
            binding.savingInput.error = "Pole jest puste"
            isErrors = true
        }
        return isErrors
    }
}