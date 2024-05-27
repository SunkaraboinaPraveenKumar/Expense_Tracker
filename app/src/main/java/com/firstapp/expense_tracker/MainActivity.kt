package com.firstapp.expense_tracker
import BudgetedCategoriesScreen
import BudgetedCategory
import DebtsScreen
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
import com.example.myapp.ui.screens.MyApp
import com.firstapp.expense_tracker.ui.theme.Expense_TrackerTheme

class MainActivity : ComponentActivity() {
    private val expenseRecords = mutableListOf<ExpenseRecord>()
    private val budgetedCategories = mutableListOf<BudgetedCategory>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Expense_TrackerTheme {
                var showAddExpenseScreen by remember { mutableStateOf(false) }
                var showViewRecordsScreen by remember { mutableStateOf(false) }
                var showSetBudgetScreen by remember { mutableStateOf(false) }
                var showBudgetedCategoriesScreen by remember { mutableStateOf(false) }
                var showDebtsScreen by remember { mutableStateOf(false) }
                var showAnalysisScreen by remember { mutableStateOf(false) }
               // val navController = rememberNavController()

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
                            expenseRecordsBudgeted = expenseRecords
                        ) {
                            // Set showBudgetedCategoriesScreen to false when navigating back
                            showBudgetedCategoriesScreen = false
                        }
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
                    showDebtsScreen -> {
                        DebtsScreen(onBack = {showDebtsScreen=false})
                    }
                    showAnalysisScreen->{
                        MyApp(onBack = {showAnalysisScreen=false},
                            expenseRecords=expenseRecords)
                    }
                    else -> {
                        ExpenseTrackerScreen(
                            onAddExpenseClick = { showAddExpenseScreen = true },
                            onViewRecordsClick = { showViewRecordsScreen = true },
                            expenseRecords = expenseRecords,
                            onSetBudgetClick = { showSetBudgetScreen = true },
                            onViewDebtsClick = { showDebtsScreen = true }, // Ensure this is correct
                            onViewAnalysisClick = {showAnalysisScreen=true},
                        )
                    }
                }
            }
        }
    }
}
