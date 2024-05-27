package com.firstapp.expense_tracker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
            ){
                Text(
                    text = "Total: $${currentMonthIncome-currentMonthExpense}",
                    fontSize = 16.sp,
                    color = Color(0xFF5C61C3), // Red color for expense,
                    modifier = Modifier.align(Alignment.CenterHorizontally) // Center the text
                )
            }
        }
    }
}
