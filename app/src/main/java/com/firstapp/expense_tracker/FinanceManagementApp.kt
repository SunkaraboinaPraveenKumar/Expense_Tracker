package com.firstapp.expense_tracker
import BudgetedCategoriesScreen
import BudgetedCategory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.financemanagementapp.DebtsScreen
import com.example.financemanagementapp.SearchExpenseScreen

private var expenseRecords = mutableListOf<ExpenseRecord>()
private val budgetedCategories = mutableListOf<BudgetedCategory>()
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FinanceManagementApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "expenseTracker") {
        composable("expenseTracker") {
            ExpenseTrackerScreen(
                navController = navController,
                onAddExpenseClick = { navController.navigate("addExpense") },
                expenseRecords = expenseRecords,
                onViewRecordsClick = { navController.navigate("viewRecords") },
                onSetBudgetClick = { navController.navigate("setBudget") },
                onViewDebtsClick = { navController.navigate("debts") },
                onViewAnalysisClick = { navController.navigate("analysis") },
                onViewFilterClick = { navController.navigate("filter") } // Navigate to filter screen
            )
        }
        composable("viewRecords") {
            ViewRecordsScreen(
                expenseRecords = expenseRecords,
                onBack = { navController.popBackStack() }
            )
        }
        composable("setBudget") {
            SetBudgetCard(
                onClose = { navController.popBackStack() },
                onBackHome = { navController.popBackStack() },
                onAddBudgetCategory = { budgetedCategory ->
                    budgetedCategories.add(budgetedCategory)
                },
                onViewBudgetedCategoriesClick = {
                    navController.navigate("budgetedCategories")
                }
            )
        }
        composable("budgetedCategories") {
            BudgetedCategoriesScreen(
                budgetedCategories = budgetedCategories,
                expenseRecordsBudgeted = expenseRecords
            ) {
                navController.popBackStack()
            }
        }
        composable("addExpense") {
            AddExpenseScreen(
                onCancel = { navController.popBackStack() }
            ) { record ->
                expenseRecords.add(record)
                navController.popBackStack()
            }
        }
        composable("debts") {
            DebtsScreen(onBack = { navController.popBackStack() })
        }
        composable("analysis") {
            MyApp(onBack = { navController.popBackStack() }, expenseRecords = expenseRecords)
        }
        composable("filter") {
            SearchExpenseScreen(
                onBack = { navController.popBackStack() },
                expenseRecords = expenseRecords
            )
        }
    }
}