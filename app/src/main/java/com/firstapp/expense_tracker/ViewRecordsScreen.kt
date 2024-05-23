package com.firstapp.expense_tracker

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ViewRecordsScreen(expenseRecords: List<ExpenseRecord>, onBack: () -> Unit) {
    // Get the current year and month
    val currentYearMonth = YearMonth.now()

    // Calculate the income and expense for the current month
    val currentMonthIncome = expenseRecords.filter {
        it.isIncome && YearMonth.from(it.dateTime) == currentYearMonth
    }.sumOf { it.amount }
    val currentMonthExpense = expenseRecords.filter {
        !it.isIncome && YearMonth.from(it.dateTime) == currentYearMonth
    }.sumOf { it.amount }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = onBack) {
                Text(text = "Back", fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        CurrentMonthCard(currentMonthIncome, currentMonthExpense)

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(expenseRecords) { record ->
                ExpenseRecordItem(record)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpenseRecordItem(record: ExpenseRecord) {
    val color = if (record.isIncome) Color.Green else Color.Red
    val sign = if (record.isIncome) "+" else "-"
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    val icon: Painter = painterResource(id = R.drawable.ic_account_balance_wallet) // Replace with actual icon

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 8.dp)
            .background(Color.LightGray, shape = MaterialTheme.shapes.small)
    ) {
        // Date section
        Text(
            text = record.dateTime.format(formatter),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = color,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .background(Color.White)
                .padding(bottom = 4.dp)
        )
        Divider(color = color, thickness = 1.dp, modifier = Modifier.padding(bottom = 8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Image(
                painter = icon,
                contentDescription = "Category Icon",
                modifier = Modifier
                    .size(40.dp)
                    .padding(8.dp),
                colorFilter = ColorFilter.tint(color)
            )

            Column(modifier = Modifier
                .weight(1f)
                .padding(8.dp)) {
                Text(text = record.category, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = record.accountType, fontSize = 14.sp)
            }

            Text(
                text = "$sign${record.amount}",
                fontSize = 16.sp,
                color = color,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}