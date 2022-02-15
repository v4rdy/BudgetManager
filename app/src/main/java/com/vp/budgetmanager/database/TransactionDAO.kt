package com.vp.budgetmanager.database

import androidx.room.*
import com.vp.budgetmanager.model.Transaction
import com.vp.budgetmanager.table_name

import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDAO {

    @Query("SELECT * FROM $table_name")
    fun getAllTransactions(): Flow<List<Transaction>>

    @Query("DELETE FROM $table_name")
    suspend fun deleteAll()

    @Query("Select SUM(amount) from $table_name")
    fun getTotalSpent(): Flow<Int>

    @Query("Select SUM(amount) from $table_name where time >= :time")
    fun getTodaySpent(time: Long): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(transaction: Transaction)

    @Delete
    suspend fun delete(transaction: Transaction)

}