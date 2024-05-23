package com.firstapp.expense_tracker

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CurrentMonthCard(
    currentMonthIncome: Double,
    currentMonthExpense: Double
) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Current Month",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Income: $${currentMonthIncome}",
                    fontSize = 16.sp,
                    color = Color(0xFF4CAF50) // Green color for income
                )
                Spacer(modifier = Modifier.width(16.dp)) // Add gap between income and expense
                Text(
                    text = "Expense: $${currentMonthExpense}",
                    fontSize = 16.sp,
                    color = Color(0xFFC37A5C) // Red color for expense
                )
            }
        }
    }
}
