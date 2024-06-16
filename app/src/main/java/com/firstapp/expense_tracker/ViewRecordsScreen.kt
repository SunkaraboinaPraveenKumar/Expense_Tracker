package com.firstapp.expense_tracker
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    var currentYearMonth by remember { mutableStateOf(YearMonth.now()) }

    // Filter the records for the current month and year
    val filteredRecords = expenseRecords.filter { record ->
        val recordYearMonth = YearMonth.from(record.dateTime)
        recordYearMonth == currentYearMonth
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

        // Header with chevron navigation
        Header(
            currentYearMonth,
            onPrevClick = { currentYearMonth = currentYearMonth.minusMonths(1) },
            onNextClick = { currentYearMonth = currentYearMonth.plusMonths(1) }
        )

        // Display income and expense for the current month
        CurrentMonthCard(currentMonthIncome, currentMonthExpense)

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