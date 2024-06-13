package com.firstapp.expense_tracker
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
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
    onViewAnalysisClick: () -> Unit
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

    Column {
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
    }
}