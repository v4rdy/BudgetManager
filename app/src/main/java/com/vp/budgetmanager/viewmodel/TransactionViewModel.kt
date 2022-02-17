package com.vp.budgetmanager.viewmodel

import android.content.Context
import androidx.lifecycle.*
import com.vp.budgetmanager.model.Transaction
import com.vp.budgetmanager.database.TransactionRepository
import com.vp.budgetmanager.utils.INCOME_AMOUNT
import com.vp.budgetmanager.utils.NAME
import com.vp.budgetmanager.utils.SAVING_AMOUNT
import com.vp.budgetmanager.utils.SharedPref
import kotlinx.coroutines.launch


class TransactionViewModel(private val repository: TransactionRepository) : ViewModel() {

    val allTransactions: LiveData<List<Transaction>> = repository.allTransactions.asLiveData()
    val totalSpent: LiveData<Float> = repository.totalSpent.asLiveData()

    fun getTodaySpent(time: Long): LiveData<Float> {
        return repository.getTodaySpent(time).asLiveData()
    }

    fun insert(transaction: Transaction) = viewModelScope.launch {
        repository.insert(transaction)
    }

    fun delete(transaction: Transaction) = viewModelScope.launch {
        repository.delete(transaction)
    }

    fun isFirstEnter(context: Context): Boolean {
        return when {
            SharedPref.getInstance(context).getString(NAME).isNullOrEmpty() -> true
            SharedPref.getInstance(context).getFloat(INCOME_AMOUNT) == 0.0F -> true
            SharedPref.getInstance(context).getFloat(SAVING_AMOUNT) == 0.0F -> true
            else -> false
        }
    }
}

class TransactionViewModelFactory(private val repository: TransactionRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransactionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}