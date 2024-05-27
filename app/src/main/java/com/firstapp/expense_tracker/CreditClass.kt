package com.firstapp.expense_tracker
import java.time.LocalDate
data class Credit(
    val amount: Double,
    val from: String,
    val description: String,
    val dateOfRepayment: LocalDate
)
