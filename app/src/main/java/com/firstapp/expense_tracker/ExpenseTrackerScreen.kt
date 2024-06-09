package com.firstapp.expense_tracker

import SettingsMenu
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpenseTrackerScreen(
    navController: NavController,
    onAddExpenseClick: () -> Unit,
    expenseRecords: List<ExpenseRecord>,
    onViewRecordsClick: () -> Unit,
    onSetBudgetClick: () -> Unit,
    onViewDebtsClick: () -> Unit,
    onViewAnalysisClick: () -> Unit,
    onViewFilterClick: () -> Unit
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    var selectedBottomTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Daily", "Monthly", "Calendar", "Notes")
    var currentYearMonth by remember { mutableStateOf(YearMonth.now()) }
    val formatter = DateTimeFormatter.ofPattern("MMM yyyy")

    // Drawer state
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    // Calculate the income and expense for the current month
    val currentMonthIncome = expenseRecords.filter {
        it.isIncome && YearMonth.from(it.dateTime) == currentYearMonth
    }.sumOf { it.amount }
    val currentMonthExpense = expenseRecords.filter {
        !it.isIncome && YearMonth.from(it.dateTime) == currentYearMonth
    }.sumOf { it.amount }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.75f)
                    .background(Color.LightGray)
                    .padding(16.dp)
            ) {
                SettingsMenu(
                    onSettingsClick = { /* Handle Settings Click */ },
                    onDeleteResetClick = { /* Handle Delete/Reset Click */ },
                    onHelpClick = { /* Handle Help Click */ },
                    onAddNewCategoriesClick = { /* Handle Add New Categories Click */ }
                )
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                TopAppBar(
                    title = {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            IconButton(onClick = {
                                coroutineScope.launch {
                                    if (drawerState.isOpen) drawerState.close()
                                    else drawerState.open()
                                }
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_menu),
                                    contentDescription = "Menu",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(onClick = onViewFilterClick) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_search),
                                    contentDescription = "Search",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    },
                    actions = { /* Other actions can be added here */ }
                )

                // Header with the current month
                Header(
                    currentYearMonth,
                    onPrevClick = { currentYearMonth = currentYearMonth.minusMonths(1) },
                    onNextClick = { currentYearMonth = currentYearMonth.plusMonths(1) }
                )

                // Tab layout
                TabLayout(tabs, selectedTabIndex) { index ->
                    selectedTabIndex = index
                }

                // Income and expense overview
                CurrentMonthCard(currentMonthIncome, currentMonthExpense)

                // Action buttons
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = onSetBudgetClick,
                        modifier = Modifier
                            .fillMaxWidth(0.45f)
                            .height(56.dp)
                    ) {
                        Text(text = "Set Budget", fontSize = 18.sp)
                    }
                    Button(
                        onClick = onViewRecordsClick,
                        modifier = Modifier
                            .fillMaxWidth(0.45f)
                            .height(56.dp)
                    ) {
                        Text(text = "View Records", fontSize = 18.sp)
                    }
                    Button(
                        onClick = onViewDebtsClick,
                        modifier = Modifier
                            .fillMaxWidth(0.45f)
                            .height(56.dp)
                    ) {
                        Text(text = "View Debts", fontSize = 18.sp)
                    }
                    Button(
                        onClick = onViewAnalysisClick,
                        modifier = Modifier
                            .fillMaxWidth(0.45f)
                            .height(56.dp)
                    ) {
                        Text(text = "View Analysis", fontSize = 18.sp)
                    }
                }

                // Floating action button
                Box(
                    contentAlignment = Alignment.BottomEnd,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    IconButton(
                        onClick = onAddExpenseClick,
                        modifier = Modifier
                            .size(64.dp)
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

                // Bottom navigation bar
                BottomNavBar(
                    selectedTabIndex = selectedBottomTabIndex,
                    onTabSelected = { index ->
                        selectedBottomTabIndex = index
                        when (index) {
                            0 -> navController.navigate("expenseTracker") // Navigate to Expense Tracker screen
                            1 -> navController.navigate("viewRecords")
                            2 -> navController.navigate("analysis")
                            3 -> navController.navigate("setBudget")
                        }
                    }
                )
            }
        }
    }
}