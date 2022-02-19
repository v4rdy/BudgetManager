package com.vp.budgetmanager

import android.app.Application
import com.vp.budgetmanager.database.TransactionRepository
import com.vp.budgetmanager.database.TransactionRoomDatabase
import com.vp.budgetmanager.viewmodel.TransactionViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class TransactionApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { TransactionRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { TransactionRepository(database.transactionDAO()) }
    val viewModelFactory by lazy {TransactionViewModelFactory(repository)}

}