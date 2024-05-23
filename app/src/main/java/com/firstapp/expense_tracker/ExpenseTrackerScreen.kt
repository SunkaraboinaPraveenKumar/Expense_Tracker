package com.firstapp.expense_tracker

import NavigationBar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpenseTrackerScreen(
    onAddExpenseClick: () -> Unit,
    expenseRecords: List<ExpenseRecord>,
    onViewRecordsClick: () -> Unit,
    onSetBudgetClick:()->Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var selectedBottomTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Daily", "Monthly", "Calendar", "Notes")
    var currentDate by remember { mutableStateOf(LocalDate.now()) }
    //val formatter = DateTimeFormatter.ofPattern("MMM dd yyyy")
    var budget by remember { mutableDoubleStateOf(5000.0) }


    var currentYearMonth by remember { mutableStateOf(YearMonth.now()) }
    val formatter = DateTimeFormatter.ofPattern("MMM yyyy")

    // Calculate the income and expense for the current month
    val currentMonthIncome = expenseRecords.filter {
        it.isIncome && YearMonth.from(it.dateTime) == currentYearMonth
    }.sumOf { it.amount }
    val currentMonthExpense = expenseRecords.filter {
        !it.isIncome && YearMonth.from(it.dateTime) == currentYearMonth
    }.sumOf { it.amount }

    Column(modifier = Modifier.fillMaxSize()) {
        NavigationBar(
            onSettingsClick = { /* Handle Settings option click */ },
            onDeleteResetClick = { /* Handle Delete and Reset option click */ },
            onHelpClick = { /* Handle Help option click */ },
            onAddNewCategoriesClick = { /* Handle Add New Categories option click */ }
        )
        Header(currentYearMonth, onPrevClick = {
            currentYearMonth = currentYearMonth.minusMonths(1)
        }) {
            currentYearMonth = currentYearMonth.plusMonths(1)
        }
        TabLayout(tabs, selectedTabIndex) { index ->
            selectedTabIndex = index
        }
        CurrentMonthCard(currentMonthIncome, currentMonthExpense)
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp) // Space between buttons
        ) {
            Button(
                onClick = onSetBudgetClick,
                modifier = Modifier
                    .fillMaxWidth(0.45f)
                    .height(56.dp) // Increased height
            ) {
                Text(text = "Set Budget", fontSize = 18.sp) // Increased text size
            }
            Button(
                onClick = onViewRecordsClick,
                modifier = Modifier
                    .fillMaxWidth(0.45f)
                    .height(56.dp) // Increased height
            ) {
                Text(text = "View Records", fontSize = 18.sp) // Increased text size
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            IconButton(
                onClick = onAddExpenseClick,
                modifier = Modifier
                    .size(64.dp) // Increased size
                    .shadow(8.dp, CircleShape)
                    .background(Color(0xFFC37A5C), CircleShape)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "Add Expense",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
        BottomNavBar(selectedTabIndex = selectedBottomTabIndex) { index ->
            selectedBottomTabIndex = index
            // Handle bottom tab click
        }
    }
}
