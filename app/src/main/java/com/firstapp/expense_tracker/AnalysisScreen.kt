package com.firstapp.expense_tracker

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AnalysisScreen(analysisType: String, expenseRecords: List<ExpenseRecord>, onBack: () -> Unit) {
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

    CustomPieChart(data = data,
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        analysisType = analysisType,
        onBack = onBack)
}

@Composable
fun MainScreen(onIncomeClick: () -> Unit, onExpenseClick: () -> Unit, onBack:()->Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
//        TextButton(onClick = onBack) {
//            Text(text = "Back", fontSize = 16.sp)
//        }
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
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
            onBack = onBack
        )
        "analysis" -> AnalysisScreen(
            analysisType = analysisType,
            expenseRecords = expenseRecords,
            onBack = onBack
        )
    }
}