package com.firstapp.expense_tracker

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddExpenseScreen(
    onCancel: () -> Unit,
    onSave: (ExpenseRecord) -> Unit
) {
    var accountType by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
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

    val textColor = if (isSystemInDarkTheme()) Color.White else Color.Black

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
                    amount = amount.toDoubleOrNull() ?: 0.0,
                    dateTime = dateTime,
                    isIncome = isIncome,
                    notes = notes,
                    date= YearMonth.now()
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
                    contentColor = if (isIncome) MaterialTheme.colorScheme.primary else textColor
                )
            ) {
                Text(text = "Income", fontSize = 18.sp)
            }
            TextButton(
                onClick = { isIncome = false },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (!isIncome) MaterialTheme.colorScheme.primary else textColor
                )
            ) {
                Text(text = "Expense", fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isIncome) "Add Income" else "Add Expense",
            fontSize = 24.sp,
            color = Color(0xFFFFA500), // Orange color
            modifier = Modifier.padding(16.dp)
        )
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
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            textStyle = LocalTextStyle.current.copy(color = Color.Black)
        )
        Text(
            text = "Date & Time: ${dateTime.format(formatter)}",
            fontSize = 16.sp,
            modifier = Modifier.padding(vertical = 8.dp),
            color = textColor
        )

        Spacer(modifier = Modifier.weight(1f))

        Calculator { newAmount ->
            amount = newAmount.toString()
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
