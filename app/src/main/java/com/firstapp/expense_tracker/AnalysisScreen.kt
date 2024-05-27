package com.example.myapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapp.ui.components.CustomPieChart
import com.firstapp.expense_tracker.ExpenseRecord

@Composable
fun AnalysisScreen(analysisType: String, expenseRecords: List<ExpenseRecord>, onBackClick: () -> Unit) {
    // Filter expense records based on analysis type
    val groupedData = if (analysisType == "Income") {
        expenseRecords.filter { it.isIncome }.groupBy({ it.category }, { it.amount.toFloat() })
    } else {
        expenseRecords.filter { !it.isIncome }.groupBy({ it.category }, { it.amount.toFloat() })
    }

    // Convert grouped data to the required format
    val data = groupedData.map { (category, amounts) ->
        category to amounts.sum()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.Black)
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black
            )
        }
        Text(
            text = "$analysisType Analysis",
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = Color.White
        )
        CustomPieChart(data = data, modifier = Modifier.fillMaxWidth().padding(16.dp))
    }
}

@Composable
fun MainScreen(onIncomeClick: () -> Unit, onExpenseClick: () -> Unit, onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IconButton(onClick = onBackClick, modifier = Modifier.align(Alignment.Start)) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.Blue
            )
        }
        Button(onClick = onIncomeClick, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Text(text = "Income Analysis")
        }
        Button(onClick = onExpenseClick, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Text(text = "Expense Analysis")
        }
    }
}
@Composable
fun MyApp(expenseRecords:List<ExpenseRecord>,onBack:()->Unit) {
    var currentScreen by remember { mutableStateOf("main") }
    var analysisType by remember { mutableStateOf("") }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextButton(onClick = onBack) {
            Text(text = "Back", fontSize = 16.sp)
        }
    }
    when (currentScreen) {
        "main" -> MainScreen(
            onIncomeClick = {
                analysisType = "Income"
                currentScreen = "analysis"
            },
            onExpenseClick = {
                analysisType = "Expense"
                currentScreen = "analysis"
            },
            onBackClick = {

            }
        )
        "analysis" -> AnalysisScreen(
            analysisType = analysisType,
            expenseRecords = expenseRecords,
            onBackClick = onBack
        )
    }
}