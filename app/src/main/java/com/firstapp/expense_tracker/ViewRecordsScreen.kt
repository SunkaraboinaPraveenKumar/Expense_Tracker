package com.firstapp.expense_tracker
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ViewRecordsScreen(
    expenseRecords: List<ExpenseRecord>,
    onEdit: (ExpenseRecord) -> Unit,
    onDelete: (ExpenseRecord) -> Unit,
    onBack: () -> Unit
) {
    var currentDate by remember { mutableStateOf(LocalDate.now()) }
    var currentFilterOption by remember { mutableStateOf(FilterOption.MONTHLY) }
    // Define date range based on filter option
    val dateRange = when (currentFilterOption) {
        FilterOption.DAILY -> currentDate to currentDate
        FilterOption.WEEKLY -> {
            val startOfWeek = currentDate.with(java.time.DayOfWeek.MONDAY)
            startOfWeek to startOfWeek.plusDays(6)
        }
        FilterOption.MONTHLY -> {
            val startOfMonth = currentDate.withDayOfMonth(1)
            val endOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth())
            startOfMonth to endOfMonth
        }
    }

    // Filter records based on date range
    val filteredIncomeRecords = expenseRecords.filter { it.isIncome && it.dateTime.toLocalDate() in dateRange.first..dateRange.second }
    val filteredExpenseRecords = expenseRecords.filter { !it.isIncome && it.dateTime.toLocalDate() in dateRange.first..dateRange.second }

    val currentIncome = filteredIncomeRecords.sumOf { it.amount }
    val currentExpense = filteredExpenseRecords.sumOf { it.amount }
    // Define the start of the week as Monday
    val startOfWeek = currentDate.with(java.time.DayOfWeek.MONDAY)
    val endOfWeek = startOfWeek.plusDays(6)

    // Filter the records based on current filter option
    val filteredRecords = remember(currentDate, currentFilterOption) {
        when (currentFilterOption) {
            FilterOption.DAILY -> expenseRecords.filter { record ->
                record.dateTime.toLocalDate() == currentDate
            }
            FilterOption.WEEKLY -> expenseRecords.filter { record ->
                val recordDate = record.dateTime.toLocalDate()
                recordDate in startOfWeek..endOfWeek
            }
            FilterOption.MONTHLY -> expenseRecords.filter { record ->
                YearMonth.from(record.dateTime) == YearMonth.from(currentDate)
            }
        }
    }

    // Calculate current month income and expenses
    val currentMonthIncome = filteredRecords.filter { it.isIncome }
        .sumOf { it.amount }
    val currentMonthExpense = filteredRecords.filter { !it.isIncome }
        .sumOf { it.amount }

    // Sort records by date
    val sortedExpenseRecords = filteredRecords.filter { record ->
        record.accountType.isNotEmpty() && record.category.isNotEmpty()
    }.sortedByDescending { it.dateTime }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        // Header with chevron navigation and filter icon
        HeaderRecord(
            currentDate = currentDate,
            currentFilterOption = currentFilterOption,
            onPrevClick = {
                currentDate = when (currentFilterOption) {
                    FilterOption.DAILY -> currentDate.minusDays(1)
                    FilterOption.WEEKLY -> currentDate.minusWeeks(1)
                    FilterOption.MONTHLY -> currentDate.minusMonths(1)
                }
            },
            onNextClick = {
                currentDate = when (currentFilterOption) {
                    FilterOption.DAILY -> currentDate.plusDays(1)
                    FilterOption.WEEKLY -> currentDate.plusWeeks(1)
                    FilterOption.MONTHLY -> currentDate.plusMonths(1)
                }
            },
            onFilterOptionSelected = {
                currentFilterOption = it
            }
        )

        // Display income and expense for the current month
        CurrentMonthCard(
            currentFilterOption = currentFilterOption,
            dateRange = dateRange,
            incomeRecords = filteredIncomeRecords,
            expenseRecords = filteredExpenseRecords
        )

        Spacer(modifier = Modifier.height(16.dp))

        // List of expense records
        LazyColumn {
            items(sortedExpenseRecords) { record ->
                ExpenseRecordItem(
                    record = record,
                    onEdit = onEdit,
                    onDelete = onDelete
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}


enum class FilterOption {
    DAILY,
    WEEKLY,
    MONTHLY
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HeaderRecord(
    currentDate: LocalDate,
    currentFilterOption: FilterOption,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit,
    onFilterOptionSelected: (FilterOption) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    // Define the date formats for different filter options
    val dailyFormatter = DateTimeFormatter.ofPattern("EEEE, MMM d, yyyy")
    val weeklyFormatter = DateTimeFormatter.ofPattern("'Week of' MMM d, yyyy")
    val monthlyFormatter = DateTimeFormatter.ofPattern("MMMM, yyyy")

    // Determine the formatter based on current filter option
    val formatter = when (currentFilterOption) {
        FilterOption.DAILY -> dailyFormatter
        FilterOption.WEEKLY -> weeklyFormatter
        FilterOption.MONTHLY -> monthlyFormatter
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFF5722))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPrevClick) {
            Image(
                painter = painterResource(id = R.drawable.ic_chevron_left),
                contentDescription = "Previous Date",
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            text = currentDate.format(formatter),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )

        IconButton(onClick = onNextClick) {
            Image(
                painter = painterResource(id = R.drawable.ic_chevron_right),
                contentDescription = "Next Date",
                modifier = Modifier.size(24.dp)
            )
        }

        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Default.List,
                contentDescription = "Filter",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                onClick = {
                    onFilterOptionSelected(FilterOption.DAILY)
                    expanded = false
                }
            ) {
                Text("Daily")
            }
            DropdownMenuItem(
                onClick = {
                    onFilterOptionSelected(FilterOption.WEEKLY)
                    expanded = false
                }
            ) {
                Text("Weekly")
            }
            DropdownMenuItem(
                onClick = {
                    onFilterOptionSelected(FilterOption.MONTHLY)
                    expanded = false
                }
            ) {
                Text("Monthly")
            }
        }
    }
}
@Composable
fun DropdownMenuItem(
    onClick: () -> Unit,
    content: @Composable () -> Unit // Added a content lambda to provide flexibility
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            content() // Invoke the content lambda provided by the caller
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpenseRecordItem(
    record: ExpenseRecord,
    onEdit: (ExpenseRecord) -> Unit,
    onDelete: (ExpenseRecord) -> Unit
) {
    val color = if (record.isIncome) Color(0xFF4CAF50) else Color.Red
    val sign = if (record.isIncome) "+" else "-"
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 8.dp)
            .background(Color.LightGray, shape = MaterialTheme.shapes.small)
    ) {
        Text(
            text = record.dateTime.format(formatter),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = color,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .background(Color.White)
                .padding(bottom = 4.dp)
        )
        Divider(color = color, thickness = 1.dp, modifier = Modifier.padding(bottom = 8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Image(
                painter = painterResource(id = record.icon),
                contentDescription = "Category Icon",
                modifier = Modifier
                    .size(40.dp)
                    .padding(8.dp)
            )

            Column(modifier = Modifier
                .weight(1f)
                .padding(8.dp)) {
                Text(text = record.category, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = record.accountType, fontSize = 14.sp)
            }

            Text(
                text = "$sign${record.amount}",
                fontSize = 16.sp,
                color = color,
                modifier = Modifier.padding(8.dp)
            )

            Row(modifier = Modifier.padding(start = 8.dp)) {
                IconButton(onClick = { onEdit(record) }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = { onDelete(record) }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}