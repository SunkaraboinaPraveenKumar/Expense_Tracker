
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
    budgetedCategories: List<BudgetedCategory>,
    expenseRecordsBudgeted: MutableList<ExpenseRecord>,
    onBack: () -> Unit
) {
    // State for the current selected month and year
    var currentYearMonth by remember { mutableStateOf(YearMonth.now()) }

    // Filtering budgeted categories for the selected month and year
    var filteredBudgetedCategories by remember {
        mutableStateOf(
            budgetedCategories.filter {
                YearMonth.from(it.monthYear) == currentYearMonth
            }
        )
    }

    // Function to update the filtered budgeted categories
    fun updateFilteredCategories(yearMonth: YearMonth) {
        filteredBudgetedCategories = budgetedCategories.filter {
            YearMonth.from(it.monthYear) == yearMonth
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // Header with month navigation
        Header(currentYearMonth, onPrevClick = {
            currentYearMonth = currentYearMonth.minusMonths(1)
            updateFilteredCategories(currentYearMonth)
        }, onNextClick = {
            currentYearMonth = currentYearMonth.plusMonths(1)
            updateFilteredCategories(currentYearMonth)
        })

        if (filteredBudgetedCategories.isEmpty()) {
            // Empty state UI
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
                androidx.compose.material3.Button(onClick = onBack) {
                    Text(text = "Go Back")
                }
            }
        } else {
            // Non-empty state: show list
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
                        isOverLimit
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BudgetedCategoryRow(budgetedCategory: BudgetedCategory, isOverLimit: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .border(1.dp, if (isOverLimit) Color.Red else Color.Gray, RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        // Category Icon
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(Color.Gray, CircleShape)
        ) {
            // Icon for the category
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
        Column {
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
        Spacer(modifier = Modifier.weight(1f))
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
    }
}
