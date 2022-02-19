package com.vp.budgetmanager.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.vp.budgetmanager.database.TransactionRepository
import com.vp.budgetmanager.model.Transaction
import com.vp.budgetmanager.utils.INCOME_AMOUNT
import com.vp.budgetmanager.utils.NAME
import com.vp.budgetmanager.utils.SAVING_AMOUNT
import com.vp.budgetmanager.utils.SharedPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.math.RoundingMode


class TransactionViewModel(private val repository: TransactionRepository) : ViewModel() {

    val allTransactions: LiveData<List<Transaction>> = repository.allTransactions.asLiveData()
    val totalSpent: LiveData<Float> = repository.totalSpent.asLiveData()

    private suspend fun getCategorySpent(category: String): Float {
        return repository.getCategorySpent(category)
    }

    fun insert(transaction: Transaction) = viewModelScope.launch {
        repository.insert(transaction)
    }

    fun delete(transaction: Transaction) = viewModelScope.launch {
        repository.delete(transaction)
    }

    fun getDataListForChart(categoryList: List<String>): MutableList<DataEntry> {
        val dataList = mutableListOf<DataEntry>()
        runBlocking {
            categoryList.map { categoryName ->
                val spentAmount = runBlocking(Dispatchers.IO){
                    getCategorySpent(categoryName)
                }
                if(spentAmount != 0.0f) {
                    Log.d("Category", "$categoryName - $spentAmount")
                    dataList.add(ValueDataEntry(categoryName, spentAmount))
                }
            }
        }
        return dataList
    }

    fun isFirstEnter(context: Context): Boolean {
        return when {
            SharedPref.getInstance(context).getString(NAME).isNullOrEmpty() -> true
            SharedPref.getInstance(context).getFloat(INCOME_AMOUNT) == 0.0F -> true
            SharedPref.getInstance(context).getFloat(SAVING_AMOUNT) == 0.0F -> true
            else -> false
        }
    }

    fun calculateBalancePercent(balance: Float, incomeAmount: Float, savingAmount: Float): String {
        return "(${
            (balance * 100 / (incomeAmount - savingAmount)).toBigDecimal()
                .setScale(1, RoundingMode.HALF_EVEN)
        }%)"
    }

    fun calculateBudgetExceededPercent(
        spentAmount: Float,
        incomeAmount: Float,
        savingAmount: Float
    ): Float {
        val maxBudget = incomeAmount - savingAmount
        val exceededValue = spentAmount - maxBudget
        return exceededValue * 100 / maxBudget
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