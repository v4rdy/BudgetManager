package com.vp.budgetmanager.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vp.budgetmanager.utils.table_name

@Entity(tableName = table_name)
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "amount") val amount: Float?,
    @ColumnInfo(name = "type") val type: String?,
    @ColumnInfo(name = "time") val time: Long?
)