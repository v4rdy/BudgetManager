package com.vp.budgetmanager

import android.app.Application
import com.vp.budgetmanager.database.TransactionRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class TransactionApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { TransactionRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { TransactionRepository(database.transactionDAO()) }

}