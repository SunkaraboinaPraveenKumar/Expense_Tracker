package com.firstapp.expense_tracker
import BudgetedCategoriesScreen
import BudgetedCategory
import MainLayout
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.financemanagementapp.SearchExpenseScreen

private var expenseRecords by mutableStateOf(mutableListOf<ExpenseRecord>()) // Mutable state for expense records
private var budgetedCategories by mutableStateOf(mutableListOf<BudgetedCategory>()) // Mutable state for budgeted categories


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FinanceManagementApp() {
    val navController = rememberNavController()
    var recordToEdit by remember { mutableStateOf<ExpenseRecord?>(null) }
    NavHost(navController = navController, startDestination = "expenseTracker") {
        composable("expenseTracker") { backStackEntry ->
            MainLayout(navController = navController, onViewFilterClick = { navController.navigate("filter") }) {
                CrossfadeScreen(backStackEntry.id) {
                    ExpenseTrackerScreen(
                        navController = navController,
                        onAddExpenseClick = {
                            recordToEdit = null
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
        }
        composable("viewRecords") { backStackEntry ->
            MainLayout(navController = navController, onViewFilterClick = { navController.navigate("filter") }) {
                CrossfadeScreen(backStackEntry.id) {
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
        }
        composable("setBudget") { backStackEntry ->
            MainLayout(navController = navController, onViewFilterClick = { navController.navigate("filter") }) {
                CrossfadeScreen(backStackEntry.id) {
                    SetBudgetCard(
                        onClose = { navController.popBackStack() },
                        onBackHome = { navController.popBackStack() },
                        expenseRecordsBudgeted = expenseRecords,
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
        }
        composable("budgetedCategories") { backStackEntry ->
            MainLayout(navController = navController, onViewFilterClick = { navController.navigate("filter") }) {
                CrossfadeScreen(backStackEntry.id) {
                    BudgetedCategoriesScreen(
                        budgetedCategories = budgetedCategories,
                        expenseRecordsBudgeted = expenseRecords,
                        onBudgetCategoryDeleted = { deletedCategory ->
                            budgetedCategories.remove(deletedCategory)
                        },
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
        composable("addExpense") { backStackEntry ->
            CrossfadeScreen(backStackEntry.id) {
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
        }
        composable("debts") { backStackEntry ->
            MainLayout(navController = navController, onViewFilterClick = { navController.navigate("filter") }) {
                CrossfadeScreen(backStackEntry.id) {
                    DebtsScreen(onBack = { navController.popBackStack() })
                }
            }
        }
        composable("analysis") { backStackEntry ->
            MainLayout(navController = navController, onViewFilterClick = { navController.navigate("filter") }) {
                CrossfadeScreen(backStackEntry.id) {
                    MyApp(onBack = { navController.popBackStack() }, expenseRecords = expenseRecords)
                }
            }
        }
        composable("filter") { backStackEntry ->
            MainLayout(navController = navController, onViewFilterClick = { navController.navigate("filter") }) {
                CrossfadeScreen(backStackEntry.id) {
                    SearchExpenseScreen(
                        expenseRecords = expenseRecords,
                        onBack = { navController.popBackStack() },
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
        composable("settings") {
            // Content for settings screen
            SettingsScreen()

        }
        composable("deleteReset") {
            // Content for delete/reset screen
        }
        composable("help") {
            // Content for help screen
        }
        composable("addCategories") {
            // Content for add new categories screen
        }
    }
}

@SuppressLint("UnusedCrossfadeTargetStateParameter")
@Composable
fun CrossfadeScreen(
    targetState: Any,
    content: @Composable () -> Unit
) {
    Crossfade(targetState = targetState, modifier = Modifier.fillMaxSize()) {
        content()
    }
}