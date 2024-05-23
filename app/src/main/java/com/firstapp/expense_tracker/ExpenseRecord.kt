package com.firstapp.expense_tracker

import java.time.LocalDateTime

data class ExpenseRecord(
    val accountType: String,
    val category: String,
    val amount: Double,
    val dateTime: LocalDateTime,
    val isIncome: Boolean,
    val notes: String
)
