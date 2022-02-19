package com.vp.budgetmanager.database

import androidx.annotation.WorkerThread
import com.vp.budgetmanager.model.Transaction
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow

@Suppress("RedundantSuspendModifier")
class TransactionRepository(private val transactionDAO: TransactionDAO) {

    val allTransactions: Flow<List<Transaction>> = transactionDAO.getAllTransactions()
    val totalSpent: Flow<Float> = transactionDAO.getTotalSpent()

    @WorkerThread
    suspend fun getCategorySpent(category: String): Float {
        return transactionDAO.getCategorySpentAsync(category)
    }

    @WorkerThread
    suspend fun insert(transaction: Transaction) {
        transactionDAO.insert(transaction)
    }

    @WorkerThread
    suspend fun delete(transaction: Transaction) {
        transactionDAO.delete(transaction)
    }

}