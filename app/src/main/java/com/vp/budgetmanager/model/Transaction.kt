package com.vp.budgetmanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vp.budgetmanager.table_name

@Entity(tableName = table_name)
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    val amount: Int,
    val type: String,
    val time: Long
)