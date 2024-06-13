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
            MainLayout(navController = navController, onViewFilterClick = { navController.navigate("filter") }) {
                ExpenseTrackerScreen(
                    navController = navController,
                    onAddExpenseClick = { navController.navigate("addExpense") },
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
                    onBack = { navController.popBackStack() }
                )
            }
        }
        composable("setBudget") {
            MainLayout(navController = navController, onViewFilterClick = { navController.navigate("filter") }) {
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
        }
        composable("budgetedCategories") {
            MainLayout(navController = navController, onViewFilterClick = { navController.navigate("filter") }) {
                BudgetedCategoriesScreen(
                    budgetedCategories = budgetedCategories,
                    expenseRecordsBudgeted = expenseRecords
                ) {
                    navController.popBackStack()
                }
            }
        }
        composable("addExpense") {
            MainLayout(navController = navController, onViewFilterClick = { navController.navigate("filter") }) {
                AddExpenseScreen(
                    onCancel = { navController.popBackStack() }
                ) { record ->
                    expenseRecords.add(record)
                    navController.popBackStack()
                }
            }
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
                    expenseRecords = expenseRecords
                )
            }
        }
    }
}