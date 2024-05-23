package com.firstapp.expense_tracker

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddExpenseScreen(
    onCancel: () -> Unit,
    onSave: (ExpenseRecord) -> Unit
) {
    var accountType by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var amount by remember { mutableDoubleStateOf(0.0) }
    val dateTime by remember { mutableStateOf(LocalDateTime.now()) }
    var isIncome by remember { mutableStateOf(false) }
    val notes by remember { mutableStateOf("") }

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    val incomeCategories = listOf(
        "Awards", "Coupons", "Grants", "Lottery", "Refunds",
        "Rental", "Salary", "Sale"
    )
    val expenseCategories = listOf(
        "Baby", "Beauty", "Bills", "Car", "Clothing", "Education",
        "Electronics", "Entertainment", "Food", "Health", "Home",
        "Insurance", "Shopping", "Social", "Sport", "Transportation"
    )
    val accountTypes = listOf("Card", "Cash", "Savings")

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = onCancel) {
                Text(text = "Cancel", fontSize = 16.sp)
            }
            TextButton(onClick = {
                val record = ExpenseRecord(
                    accountType = accountType,
                    category = category,
                    amount = amount,
                    dateTime = dateTime,
                    isIncome = isIncome,
                    notes = notes
                )
                onSave(record)
            }) {
                Text(text = "Save", fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            TextButton(
                onClick = { isIncome = true },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (isIncome) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            ) {
                Text(text = "Income", fontSize = 18.sp)
            }
            TextButton(
                onClick = { isIncome = false },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (!isIncome) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            ) {
                Text(text = "Expense", fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = if (isIncome) "Add Income" else "Add Expense", fontSize = 24.sp, modifier = Modifier.padding(16.dp))

        DropdownMenuField(
            options = accountTypes,
            selectedOption = accountType,
            onOptionSelected = { accountType = it },
            label = "Account Type"
        )

        DropdownMenuField(
            options = if (isIncome) incomeCategories else expenseCategories,
            selectedOption = category,
            onOptionSelected = { category = it },
            label = "Category"
        )

        TextField(
            value = amount.toString(),
            onValueChange = { amount = it.toDoubleOrNull() ?: 0.0 },
            label = { Text("Amount") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )
        Text(
            text = "Date & Time: ${dateTime.format(formatter)}",
            fontSize = 16.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Calculator { newAmount ->
            amount = newAmount
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DropdownMenuField(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    label: String
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        TextField(
            value = selectedOption,
            onValueChange = { },
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clickable { expanded = true },
            readOnly = true
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = option,
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun DropdownMenuItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Text(
            text = text,
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp)
        )
    }
}
