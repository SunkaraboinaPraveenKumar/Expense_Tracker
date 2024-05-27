
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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.firstapp.expense_tracker.Header
import com.firstapp.expense_tracker.R
import java.time.YearMonth

// Sample list of categories
val categories = listOf(
    "Beauty", "Bills", "Car", "Clothing",
    "Education", "Electronics", "Entertainment",
    "Food", "Health", "Home", "Insurance",
    "Shopping", "Social", "Sport", "Tax",
    "Telephone", "Transportation"
)


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SetBudgetCard(
    onClose: () -> Unit,
    onBackHome: () -> Unit,
    onAddBudgetCategory: (BudgetedCategory) -> Unit,
    onViewBudgetedCategoriesClick: () -> Unit
) {
    var selectedMonthYear by remember { mutableStateOf(YearMonth.now()) }
    var isDialogVisible by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
//    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        IconButton(onClick = onBackHome) {
            Icon(
                painter = painterResource(id = R.drawable.ic_chevron_left),
                contentDescription = "Back"
            )
        }
        Button(
            onClick = onViewBudgetedCategoriesClick,
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 16.dp, top = 16.dp)
        ) {
            Text(text = "View Budgeted Categories")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            item {
                Header(
                    currentDate = selectedMonthYear,
                    onPrevClick = { selectedMonthYear = selectedMonthYear.minusMonths(1) },
                    onNextClick = { selectedMonthYear = selectedMonthYear.plusMonths(1) }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Categories",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(categories) { category ->
                BudgetCategoryRow(category = category) {
                    selectedCategory = category
                    isDialogVisible = true
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    if (isDialogVisible && selectedCategory != null) {
        SetBudgetDialog(
            category = selectedCategory!!,
            onCloseDialog = { isDialogVisible = false },
            onSetBudget = { limit ->
                val newBudgetedCategory = BudgetedCategory(
                    category = selectedCategory!!,
                    limit = limit,
                    spent = 0.0,
                    remaining = limit,
                    monthYear = selectedMonthYear
                )
                onAddBudgetCategory(newBudgetedCategory)
                isDialogVisible = false
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BudgetCategoryRow(category: String, onSetBudgetClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        // Category Icon
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(Color.Gray, CircleShape)
        ) {
            // Icon for the category (you can replace the placeholder)
            Image(
                painter = painterResource(id = R.drawable.ic_category),
                contentDescription = "Category Icon",
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = category,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onSetBudgetClick,
            modifier = Modifier
                .padding(start = 8.dp)
                .height(36.dp)
        ) {
            Text(text = "Set Budget")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SetBudgetDialog(
    category: String,
    onCloseDialog: () -> Unit,
    onSetBudget: (Double) -> Unit
) {
    var budgetLimit by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onCloseDialog) { // Invoke onClose when the dialog is dismissed
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Text(
                text = "Set Budget for $category",
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
                        painter = painterResource(id = R.drawable.ic_category),
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
                value = budgetLimit,
                onValueChange = { budgetLimit = it },
                label = { Text(text = "Budget Limit") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = onCloseDialog) { // Invoke onClose when cancel button is clicked
                    Text(text = "Cancel")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    val limit = budgetLimit.toDoubleOrNull()
                    if (limit != null) {
                        onSetBudget(limit)
                    }
                }) {
                    Text(text = "Set")
                }
            }
        }
    }
}
