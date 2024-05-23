package com.firstapp.expense_tracker

import SetBudgetCard
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import com.firstapp.expense_tracker.ui.theme.Expense_TrackerTheme


class MainActivity : ComponentActivity() {
    private val expenseRecords = mutableListOf<ExpenseRecord>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Expense_TrackerTheme {
                var showAddExpenseScreen by remember { mutableStateOf(false) }
                var showViewRecordsScreen by remember { mutableStateOf(false) }
                var showSetBudgetScreen by remember { mutableStateOf(false) }
                when {
                    showSetBudgetScreen -> {
                        SetBudgetCard(onClose = { showSetBudgetScreen = false },
                            onBackHome = { showSetBudgetScreen = false })
                    }
                    showAddExpenseScreen -> {
                        AddExpenseScreen(
                            onCancel = { showAddExpenseScreen = false }
                        ) { record ->
                            expenseRecords.add(record)
                            showAddExpenseScreen = false
                        }
                    }
                    showViewRecordsScreen -> {
                        ViewRecordsScreen(
                            expenseRecords = expenseRecords,
                            onBack = { showViewRecordsScreen = false }
                        )
                    }
                    else -> {
                        ExpenseTrackerScreen(
                            onAddExpenseClick = { showAddExpenseScreen = true },
                            onViewRecordsClick = { showViewRecordsScreen = true },
                            expenseRecords = expenseRecords,
                            onSetBudgetClick={showSetBudgetScreen=true},
                        )
                    }
                }
            }
        }
    }
}
