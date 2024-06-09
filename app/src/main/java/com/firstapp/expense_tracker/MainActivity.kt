package com.firstapp.expense_tracker
import BudgetedCategoriesScreen
import BudgetedCategory
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.financemanagementapp.DebtsScreen
import com.example.financemanagementapp.SearchExpenseScreen
import com.firstapp.expense_tracker.ui.theme.Expense_TrackerTheme

class MainActivity : ComponentActivity() {
    private var expenseRecords = mutableListOf<ExpenseRecord>()
    private val budgetedCategories = mutableListOf<BudgetedCategory>()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Expense_TrackerTheme {
                var showAddExpenseScreen by remember { mutableStateOf(false) }
                var showFilterScreen by remember { mutableStateOf(false) }
                var showViewRecordsScreen by remember { mutableStateOf(false) }
                var showSetBudgetScreen by remember { mutableStateOf(false) }
                var showBudgetedCategoriesScreen by remember { mutableStateOf(false) }
                var showDebtsScreen by remember { mutableStateOf(false) }
                var showAnalysisScreen by remember { mutableStateOf(false) }
               // val navController = rememberNavController()
                expenseRecords = expenseRecords.filter { record ->
                    record.accountType.isNotEmpty() && record.category.isNotEmpty()
                }.toMutableList()
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
                    showFilterScreen->{
                        SearchExpenseScreen(expenseRecords = expenseRecords,onBack={showFilterScreen=false})
                    }
                    else -> {
                        ExpenseTrackerScreen(
                            onAddExpenseClick = { showAddExpenseScreen = true },
                            onViewRecordsClick = { showViewRecordsScreen = true },
                            expenseRecords = expenseRecords,
                            onSetBudgetClick = { showSetBudgetScreen = true },
                            onViewDebtsClick = { showDebtsScreen = true }, // Ensure this is correct
                            onViewAnalysisClick = {showAnalysisScreen=true},
                            onViewFilterClick = {showFilterScreen=true},
                        )
                    }
                }
            }
        }
    }
}
