package com.firstapp.expense_tracker

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Accounts : Screen("accounts")
    object Analysis : Screen("analysis")
    object Debts : Screen("debts")
}
