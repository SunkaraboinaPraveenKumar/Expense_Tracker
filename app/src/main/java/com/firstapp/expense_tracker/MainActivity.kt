package com.firstapp.expense_tracker

import BudgetedCategoriesScreen
import BudgetedCategory
import SetBudgetCard
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.firstapp.expense_tracker.ui.theme.Expense_TrackerTheme

class MainActivity : ComponentActivity() {
    private val expenseRecords = mutableListOf<ExpenseRecord>()
    private val budgetedCategories = mutableListOf<BudgetedCategory>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Expense_TrackerTheme {
                val navController = rememberNavController() // Initialize NavControlle
                var showAddExpenseScreen by remember { mutableStateOf(false) }
                var showViewRecordsScreen by remember { mutableStateOf(false) }
                var showSetBudgetScreen by remember { mutableStateOf(false) }
                var showBudgetedCategoriesScreen by remember { mutableStateOf(false) }

                when {
                    showSetBudgetScreen -> {
                        SetBudgetCard(
                            onClose = { showSetBudgetScreen = false },
                            onBackHome = { showSetBudgetScreen = false },
                            onAddBudgetCategory = { budgetedCategory ->
                                budgetedCategories.add(budgetedCategory)
                            },
                            onViewBudgetedCategoriesClick = {
                                showSetBudgetScreen = false
                                showBudgetedCategoriesScreen = true
                            }
                        )
                    }
                    showBudgetedCategoriesScreen -> {
                        BudgetedCategoriesScreen(
                            budgetedCategories = budgetedCategories,
                            onBack = {
                                // Set showBudgetedCategoriesScreen to false when navigating back
                                showBudgetedCategoriesScreen = false
                            }
                        )
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
                            onSetBudgetClick = { showSetBudgetScreen = true },
                            navController = navController // Pass NavController as a parameter
                        )
                    }
                }
            }
        }
    }
}
