package com.firstapp.expense_tracker
import BudgetedCategoriesScreen
import BudgetedCategory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.financemanagementapp.DebtsScreen
import com.example.financemanagementapp.SearchExpenseScreen

private var expenseRecords by mutableStateOf(mutableListOf<ExpenseRecord>()) // Mutable state for expense records
private var budgetedCategories by mutableStateOf(mutableListOf<BudgetedCategory>()) // Mutable state for budgeted categories

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FinanceManagementApp() {
    val navController = rememberNavController()
    var recordToEdit by remember { mutableStateOf<ExpenseRecord?>(null) }

    NavHost(navController = navController, startDestination = "expenseTracker") {
        composable("expenseTracker") {
            MainLayout(navController = navController, onViewFilterClick = { navController.navigate("filter") }) {
                ExpenseTrackerScreen(
                    navController = navController,
                    onAddExpenseClick = {
                        recordToEdit = null // Reset recordToEdit when adding a new expense
                        navController.navigate("addExpense")
                    },
                    expenseRecords = expenseRecords,
                    onViewRecordsClick = { navController.navigate("viewRecords") },
                    onSetBudgetClick = { navController.navigate("setBudget") },
                    onViewDebtsClick = { navController.navigate("debts") },
                    onViewAnalysisClick = { navController.navigate("analysis") }
                )
            }
        }
        composable("viewRecords") {
            MainLayout(navController = navController, onViewFilterClick = { navController.navigate("filter") }) {
                ViewRecordsScreen(
                    expenseRecords = expenseRecords,
                    onEdit = { record ->
                        recordToEdit = record
                        navController.navigate("addExpense")
                    },
                    onDelete = { record ->
                        expenseRecords = expenseRecords.filter { it.dateTime != record.dateTime }.toMutableList()
                    },
                    onBack = { navController.popBackStack() }
                )
            }
        }
        composable("setBudget") {
            MainLayout(navController = navController, onViewFilterClick = { navController.navigate("filter") }) {
                SetBudgetCard(
                    onClose = { navController.popBackStack() },
                    onBackHome = { navController.popBackStack() },
                    budgetedCategories = budgetedCategories, // Pass mutable list
                    onAddBudgetCategory = { budgetedCategory ->
                        budgetedCategories.add(budgetedCategory) // Directly add to mutable list
                    },
                    onViewBudgetedCategoriesClick = {
                        navController.navigate("budgetedCategories")
                    }
                )
            }
        }
        composable("budgetedCategories") {
            MainLayout(navController = navController, onViewFilterClick = { navController.navigate("filter") }) {
                BudgetedCategoriesScreen(
                    budgetedCategories = budgetedCategories,
                    expenseRecordsBudgeted = expenseRecords,
                    onBudgetCategoryDeleted = { deletedCategory ->
                        budgetedCategories.remove(deletedCategory) // Remove from mutable list
                    },
                    onBack = { navController.popBackStack() }
                )
            }
        }
        composable("addExpense") {
            AddExpenseScreen(
                initialRecord = recordToEdit,
                onCancel = { navController.popBackStack() },
                onSave = { newRecord ->
                    if (recordToEdit != null) {
                        expenseRecords.removeIf { it.dateTime == recordToEdit!!.dateTime }
                    }
                    expenseRecords.add(newRecord)
                    navController.popBackStack()
                }
            )
        }
        composable("debts") {
            MainLayout(navController = navController, onViewFilterClick = { navController.navigate("filter") }) {
                DebtsScreen(onBack = { navController.popBackStack() })
            }
        }
        composable("analysis") {
            MainLayout(navController = navController, onViewFilterClick = { navController.navigate("filter") }) {
                MyApp(onBack = { navController.popBackStack() }, expenseRecords = expenseRecords)
            }
        }
        composable("filter") {
            MainLayout(navController = navController, onViewFilterClick = { navController.navigate("filter") }) {
                SearchExpenseScreen(
                    onBack = { navController.popBackStack() },
                    expenseRecords = expenseRecords,
                    onEdit = { record ->
                        recordToEdit = record
                        navController.navigate("addExpense")
                    },
                    onDelete = { record ->
                        expenseRecords = expenseRecords.filter { it.dateTime != record.dateTime }.toMutableList()
                    }
                )
            }
        }
    }
}