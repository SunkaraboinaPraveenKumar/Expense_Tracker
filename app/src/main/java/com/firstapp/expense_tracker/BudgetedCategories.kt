
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.firstapp.expense_tracker.ExpenseRecord
import com.firstapp.expense_tracker.Header
import com.firstapp.expense_tracker.Icon
import com.firstapp.expense_tracker.R
import java.time.YearMonth

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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BudgetedCategoriesScreen(
    budgetedCategories: MutableList<BudgetedCategory>,
    expenseRecordsBudgeted: MutableList<ExpenseRecord>,
    onBack: () -> Unit,
    onBudgetCategoryDeleted: (BudgetedCategory) -> Unit
) {
    var currentYearMonth by remember { mutableStateOf(YearMonth.now()) }
    var filteredBudgetedCategories by remember {
        mutableStateOf(
            budgetedCategories.filter {
                YearMonth.from(it.monthYear) == currentYearMonth
            }
        )
    }
    var editingCategory by remember { mutableStateOf<BudgetedCategory?>(null) }

    // Update filtered categories whenever the month changes
    fun updateFilteredCategories(yearMonth: YearMonth) {
        filteredBudgetedCategories = budgetedCategories.filter {
            YearMonth.from(it.monthYear) == yearMonth
        }
    }

    // Handle editing a category
    fun handleEdit(budgetedCategory: BudgetedCategory) {
        editingCategory = budgetedCategory
    }

    // Handle deleting a category
    fun handleDelete(budgetedCategory: BudgetedCategory) {
        budgetedCategories.removeIf { it.dateTime == budgetedCategory.dateTime }
        updateFilteredCategories(currentYearMonth)
        onBudgetCategoryDeleted(budgetedCategory)
    }

    // Calculate the total income for the current month
    val totalIncome = expenseRecordsBudgeted
        .filter { it.isIncome && YearMonth.from(it.dateTime) == currentYearMonth }
        .sumOf { it.amount }

    // Calculate the total budgeted amount and the total spent amount for the progress bar
    val totalBudgeted = filteredBudgetedCategories.sumOf { it.limit }
    val totalSpent = filteredBudgetedCategories.sumOf { budgetedCategory ->
        expenseRecordsBudgeted
            .filter {
                it.category == budgetedCategory.category &&
                        YearMonth.from(it.date) == YearMonth.from(budgetedCategory.monthYear)
            }
            .sumOf { it.amount }
    }
    val budgetUtilization = if (totalBudgeted > 0) (totalSpent / totalBudgeted).toFloat() else 0f

    Column(
        modifier = Modifier
            .background(Color.White)
    ) {
        // Header to navigate between months
        Header(currentYearMonth, onPrevClick = {
            currentYearMonth = currentYearMonth.minusMonths(1)
            updateFilteredCategories(currentYearMonth)
        }, onNextClick = {
            currentYearMonth = currentYearMonth.plusMonths(1)
            updateFilteredCategories(currentYearMonth)
        })

        // Card showing total income
        IncomeCard(totalIncome = totalIncome)

        // Progress bar showing budget utilization
        BudgetUtilizationProgressBar(
            totalBudgeted = totalBudgeted,
            totalSpent = totalSpent,
            budgetUtilization = budgetUtilization
        )

        if (filteredBudgetedCategories.isEmpty()) {
            NoBudgetedCategoriesScreen(onBack)
        } else {
            BudgetedCategoriesList(
                filteredBudgetedCategories,
                expenseRecordsBudgeted,
                currentYearMonth,
                ::handleEdit,
                ::handleDelete
            )
        }

        if (editingCategory != null) {
            EditBudgetedCategoryDialog(
                budgetedCategory = editingCategory!!,
                onDismiss = { editingCategory = null },
                onSave = { updatedCategory ->
                    budgetedCategories.replaceAll {
                        if (it.dateTime == updatedCategory.dateTime) updatedCategory else it
                    }
                    updateFilteredCategories(currentYearMonth)
                    editingCategory = null
                }
            )
        }
    }
}
@Composable
fun IncomeCard(totalIncome: Double) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .background(Color(0xFFE3F2FD), RoundedCornerShape(8.dp)) // Light blue background
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "Total Income",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2) // Dark blue text
            )
            Text(
                text = "$${String.format("%.2f", totalIncome)}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2)
            )
        }
    }
}
@Composable
fun BudgetUtilizationProgressBar(
    totalBudgeted: Double,
    totalSpent: Double,
    budgetUtilization: Float
) {
    // Get the current density to convert mm to dp
    val density = LocalDensity.current

    // Convert mm to dp
    val minWidthDp = with(density) { mmToDp(10f) }
    val maxWidthDp = with(density) { mmToDp(20f) }

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Budget Utilization",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        LinearProgressIndicator(
            progress = budgetUtilization,
            modifier = Modifier
                .fillMaxWidth()
                .height(maxWidthDp.coerceAtMost(minWidthDp)) // Ensure height is within 10mm to 20mm
                .padding(vertical = 8.dp)
                .border(1.dp, Color.Gray, shape = RoundedCornerShape(4.dp)),
            color = if (budgetUtilization >= 1f) Color.Red else Color.Green,
            trackColor = Color.LightGray
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Spent: $${formatAmount(totalSpent)}",
                fontSize = 20.sp,
                color = Color.Red // Spent amount in red
            )
            Text(
                text = "Budgeted: $${formatAmount(totalBudgeted)}",
                fontSize = 20.sp,
                color = Color.Green // Budgeted amount in green
            )
        }
    }
}

// Function to convert millimeters to dp
@Composable
fun mmToDp(mm: Float): Dp {
    val mmInInches = mm / 25.4f // Convert mm to inches
    val dpi = LocalDensity.current.density * 80 // Get the screen DPI (density)
    val pixels = mmInInches * dpi // Convert inches to pixels
    return with(LocalDensity.current) { pixels.toDp() }
}

// Function to format amount, ensuring responsive design
fun formatAmount(amount: Double): String {
    return if (amount >= 1_000_000) {
        String.format("%,.2fM", amount / 1_000_000)
    } else if (amount >= 1_000) {
        String.format("%,.2fK", amount / 1_000)
    } else {
        String.format("%,.2f", amount)
    }
}
@Composable
fun NoBudgetedCategoriesScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No Budgeted Categories Available",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBack) {
            Text(text = "Go Back")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BudgetedCategoriesList(
    filteredBudgetedCategories: List<BudgetedCategory>,
    expenseRecordsBudgeted: List<ExpenseRecord>,
    currentYearMonth: YearMonth,
    onEditClick: (BudgetedCategory) -> Unit,
    onDeleteClick: (BudgetedCategory) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(filteredBudgetedCategories) { budgetedCategory ->
            val totalSpent = expenseRecordsBudgeted
                .filter {
                    it.category == budgetedCategory.category &&
                            YearMonth.from(it.date) == YearMonth.from(budgetedCategory.monthYear)
                }
                .sumOf { it.amount }

            val isOverLimit = totalSpent > budgetedCategory.limit
            BudgetedCategoryRow(
                budgetedCategory.copy(
                    spent = totalSpent,
                    remaining = budgetedCategory.limit - totalSpent
                ),
                isOverLimit,
                onEditClick = { onEditClick(it) },
                onDeleteClick = { onDeleteClick(it) }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BudgetedCategoryRow(
    budgetedCategory: BudgetedCategory,
    isOverLimit: Boolean,
    onEditClick: (BudgetedCategory) -> Unit,
    onDeleteClick: (BudgetedCategory) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .fillMaxWidth()
            .border(1.dp, if (isOverLimit) Color.Red else Color.Gray, RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Category Icon
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(Color.Gray, CircleShape)
            ) {
                val icon = expenseList.find { it.name == budgetedCategory.category }
                if (icon != null) {
                    Image(
                        painter = painterResource(id = icon.resourceId),
                        contentDescription = "Category Icon",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = budgetedCategory.category,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "Month: ${budgetedCategory.monthYear.monthValue}, Year: ${budgetedCategory.monthYear.year}",
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "Limit: ${budgetedCategory.limit}",
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Text(
                    text = "Spent: ${budgetedCategory.spent}",
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Text(
                    text = "Remaining: ${budgetedCategory.remaining}",
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }

            Column {
                IconButton(
                    onClick = { onEditClick(budgetedCategory) },
                    modifier = Modifier
                        .padding(5.dp)
                        .background(
                            if (isOverLimit) Color.Yellow else Color.Transparent,
                            shape = CircleShape
                        ) // Highlight edit button only if over limit
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = if (isOverLimit) Color.Black else Color.Gray // Change icon color based on over limit
                    )
                }
                IconButton(
                    onClick = { onDeleteClick(budgetedCategory) },
                    modifier = Modifier.padding(5.dp)
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Progress bar for budget
        val progress = (budgetedCategory.spent / budgetedCategory.limit).toFloat().coerceIn(0f, 1f)
        val progressBarColor = if (isOverLimit) Color.Red else Color.Green
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .border(1.dp, if (isOverLimit) Color.Red else Color.Green, shape = RoundedCornerShape(4.dp))
                .padding(horizontal = 4.dp),
            color = progressBarColor,
            trackColor = Color.LightGray
        )
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditBudgetedCategoryDialog(
    budgetedCategory: BudgetedCategory,
    onDismiss: () -> Unit,
    onSave: (BudgetedCategory) -> Unit
) {
    var category by remember { mutableStateOf(budgetedCategory.category) }
    var limit by remember { mutableStateOf(budgetedCategory.limit.toString()) }

    val iconImage = expenseList.find { it.name == budgetedCategory.category }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Text(
                text = "Edit Budgeted Category",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color.Gray, CircleShape)
                ) {
                    Image(
                        painter = painterResource(id = iconImage?.resourceId ?: R.drawable.ic_category),
                        contentDescription = "Category Icon",
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = category,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
            OutlinedTextField(
                value = limit,
                onValueChange = { limit = it },
                label = { Text(text = "Budget Limit") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = onDismiss) {
                    Text(text = "Cancel")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    val limitValue = limit.toDoubleOrNull()
                    if (limitValue != null) {
                        onSave(
                            budgetedCategory.copy(
                                category = category,
                                limit = limitValue
                            )
                        )
                    }
                }) {
                    Text(text = "Save")
                }
            }
        }
    }
}

