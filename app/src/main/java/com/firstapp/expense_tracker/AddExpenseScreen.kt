package com.firstapp.expense_tracker

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter

data class Icon(val name: String, val resourceId: Int)
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
    val accountList: MutableList<Icon> = mutableListOf(
        Icon("Card", R.drawable.credit_card),
        Icon("Cash", R.drawable.money),
        Icon("Savings", R.drawable.piggy_bank)
    )
    val incomeList: MutableList<Icon> = mutableListOf(
        Icon("Awards", R.drawable.trophy),
        Icon("Coupons", R.drawable.coupons),
        Icon("Grants", R.drawable.grants),
        Icon("Lottery", R.drawable.lottery),
        Icon("Refunds", R.drawable.refund),
        Icon("Rental", R.drawable.rental),
        Icon("Salary", R.drawable.salary),
        Icon("Sale", R.drawable.sale)
    )

    val expenseList: MutableList<Icon> = mutableListOf(
        Icon("Baby", R.drawable.milk_bottle),
        Icon("Beauty", R.drawable.beauty),
        Icon("Bills", R.drawable.bill),
        Icon("Car", R.drawable.car_wash),
        Icon("Clothing", R.drawable.clothes_hanger),
        Icon("Education", R.drawable.education),
        Icon("Electronics", R.drawable.cpu),
        Icon("Entertainment", R.drawable.confetti),
        Icon("Food", R.drawable.diet),
        Icon("Health", R.drawable.better_health),
        Icon("Home", R.drawable.house),
        Icon("Insurance", R.drawable.insurance),
        Icon("Shopping", R.drawable.bag),
        Icon("Social", R.drawable.social_media),
        Icon("Sport", R.drawable.trophy),
        Icon("Transportation", R.drawable.transportation)
    )

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
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
                    date = YearMonth.now(),
                    icon = if (isIncome) incomeList.find { it.name == category }?.resourceId ?: R.drawable.ic_account_balance_wallet else expenseList.find { it.name == category }?.resourceId ?: R.drawable.ic_account_balance_wallet
                )
                onSave(record)
            }) {
                Text(text = "Save", fontSize = 16.sp)
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            TextButton(
                onClick = {
                    isIncome = true
                    accountType = ""
                    category = ""
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (isIncome) MaterialTheme.colorScheme.primary else textColor
                )
            ) {
                Text(text = "Income", fontSize = 18.sp)
            }
            TextButton(
                onClick = {
                    isIncome = false
                    accountType = ""
                    category = ""
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (!isIncome) MaterialTheme.colorScheme.primary else textColor
                )
            ) {
                Text(text = "Expense", fontSize = 18.sp)
            }
        }

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
            label = "Account Type",
            passList = accountList
        )

        DropdownMenuField(
            options = if (isIncome) incomeCategories else expenseCategories,
            selectedOption = category,
            onOptionSelected = { category = it },
            label = "Category",
            passList = if (isIncome) incomeList else expenseList
        )

        TextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            textStyle = LocalTextStyle.current.copy(color = Color.Black)
        )
        Text(
            text = "Date & Time: ${dateTime.format(formatter)}",
            fontSize = 16.sp,
            modifier = Modifier.padding(vertical = 8.dp),
            color = textColor
        )

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Calculator { newAmount ->
                amount = newAmount.toString()
            }
        }
    }
}
@Composable
fun DropdownMenuField(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    label: String,
    passList:List<Icon>
) {
    var expanded by remember { mutableStateOf(false) }
    val backgroundColor = Color(0xFFADD8E6) // Light gray color
    Box {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(backgroundColor)
                .clickable { expanded = true },
            color = backgroundColor
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable { expanded = true },
                horizontalArrangement = Arrangement.SpaceBetween,

                ) {
                Text(text = if (selectedOption.isEmpty()) label else selectedOption)
                // You can add an icon here to indicate the dropdown
                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {

            options.forEach { option ->
                val icon=passList.find{it.name==option}
                if (icon != null) {
                    DropdownMenuItem(
                        icon = icon.resourceId,
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
}

@Composable
fun DropdownMenuItem(
    icon: Int,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null, // Provide appropriate content description
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = text,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}