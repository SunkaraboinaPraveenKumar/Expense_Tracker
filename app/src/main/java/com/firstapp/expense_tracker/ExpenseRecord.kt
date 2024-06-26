package com.firstapp.expense_tracker

import java.time.LocalDateTime
import java.time.YearMonth

data class ExpenseRecord(
    val accountType: String,
    val category: String,
    val icon: Int,
    val amount: Double,
    val dateTime: LocalDateTime,
    val isIncome: Boolean,
    val notes: String,
    val date:YearMonth
)
