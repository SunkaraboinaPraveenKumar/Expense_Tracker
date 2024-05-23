import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import java.time.YearMonth
import androidx.compose.ui.text.font.FontWeight
import com.firstapp.expense_tracker.Header
import com.firstapp.expense_tracker.R

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
fun SetBudgetCard(onClose: () -> Unit, onBackHome: () -> Unit) {
    var selectedMonthYear by remember { mutableStateOf(YearMonth.now()) }
    var isDialogVisible by remember { mutableStateOf(false) } // State to control dialog visibility

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Back button
        IconButton(onClick = onBackHome) {
            Icon(
                painter = painterResource(id = R.drawable.ic_chevron_left), // Replace with your actual back icon resource
                contentDescription = "Back"
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
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
                    isDialogVisible = true // Show the dialog when a category is clicked
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    if (isDialogVisible) {
        SetBudgetDialog(onCloseDialog = {
            isDialogVisible = false // Close the dialog when canceled or set
        })
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
fun SetBudgetDialog(onCloseDialog: () -> Unit) {
    var monthYear by remember { mutableStateOf(YearMonth.now()) }
    var budgetLimit by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onCloseDialog) { // Invoke onClose when the dialog is dismissed
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(
                text = "Set Budget",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            // Icon and category name in a box border of greenish
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .background(Color.Green, shape = RoundedCornerShape(8.dp))
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = "Category Name",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    // Input for budget limit
                    OutlinedTextField(
                        value = budgetLimit,
                        onValueChange = { budgetLimit = it },
                        label = { Text(text = "Budget Limit") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    // Month and year
                    Text(
                        text = "Month: ${monthYear.monthValue}, Year: ${monthYear.year}",
                        fontSize = 14.sp,
                        color = Color.Black,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    // Buttons for cancel and set
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextButton(onClick = onCloseDialog) { // Invoke onClose when cancel button is clicked
                            Text(text = "Cancel")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = onCloseDialog) { // Invoke onClose when set button is clicked
                            Text(text = "Set")
                        }
                    }
                }
            }
        }
    }
}
