package com.vp.budgetmanager.database
import androidx.annotation.WorkerThread
import com.vp.budgetmanager.database.TransactionDAO
import com.vp.budgetmanager.model.Transaction
import kotlinx.coroutines.flow.Flow

class TransactionRepository(private val transactionDAO: TransactionDAO) {

    val allTransactions: Flow<List<Transaction>> = transactionDAO.getAllTransactions()
    val totalSpent: Flow<Float> = transactionDAO.getTotalSpent()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun getTodaySpent(time: Long): Flow<Float>{
        return transactionDAO.getTodaySpent(time)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(transaction: Transaction) {
        transactionDAO.insert(transaction)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(transaction: Transaction) {
        transactionDAO.delete(transaction)
    }

}