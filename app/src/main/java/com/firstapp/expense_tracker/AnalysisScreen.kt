package com.firstapp.expense_tracker

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AnalysisScreen(
    analysisType: String,
    expenseRecords: List<ExpenseRecord>,
    currentYearMonth: YearMonth,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit,
    onBack: () -> Unit
) {
    // Filter expense records based on analysis type and selected month
    val filteredRecords = if (analysisType == "Income") {
        expenseRecords.filter { it.isIncome && YearMonth.from(it.dateTime) == currentYearMonth }
    } else {
        expenseRecords.filter { !it.isIncome && YearMonth.from(it.dateTime) == currentYearMonth }
    }

    val groupedData = filteredRecords.groupBy({ it.category }, { it.amount.toFloat() })
    val data = groupedData.map { (category, amounts) ->
        category to amounts.sum()
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        HeaderRecord(
            currentYearMonth,
            onPrevClick = onPrevClick,
            onNextClick = onNextClick
        )

        CustomPieChart(
            data = data,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            analysisType = analysisType,
            onBack = onBack
        )
    }
}

@Composable
fun MainScreen(onIncomeClick: () -> Unit, onExpenseClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = onIncomeClick, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Text(text = "Income Analysis")
        }
        Button(onClick = onExpenseClick, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Text(text = "Expense Analysis")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyApp(expenseRecords: List<ExpenseRecord>, onBack: () -> Unit) {
    var currentScreen by remember { mutableStateOf("main") }
    var analysisType by remember { mutableStateOf("") }
    var currentYearMonth by remember { mutableStateOf(YearMonth.now()) }

    when (currentScreen) {
        "main" -> MainScreen(
            onIncomeClick = {
                analysisType = "Income"
                currentScreen = "analysis"
            },
            onExpenseClick = {
                analysisType = "Expense"
                currentScreen = "analysis"
            }
        )
        "analysis" -> AnalysisScreen(
            analysisType = analysisType,
            expenseRecords = expenseRecords,
            currentYearMonth = currentYearMonth,
            onPrevClick = { currentYearMonth = currentYearMonth.minusMonths(1) },
            onNextClick = { currentYearMonth = currentYearMonth.plusMonths(1) },
            onBack = {
                currentScreen = "main"
                currentYearMonth = YearMonth.now()  // Reset to current month when going back
            }
        )
    }
}

// Assuming you have a `Header` function defined similarly to this:
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HeaderRecord(
    currentYearMonth: YearMonth,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("MMMM, yyyy")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFF5722))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPrevClick) {
            Image(
                painter = painterResource(id = R.drawable.ic_chevron_left),
                contentDescription = "Previous Date",
            )
        }

        Text(
            text = currentYearMonth.format(formatter),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.weight(1f),
            textAlign= TextAlign.Center
        )

        IconButton(onClick = onNextClick) {
            Image(
                painter = painterResource(id = R.drawable.ic_chevron_right),
                contentDescription = "Next Date",
            )
        }
    }
}