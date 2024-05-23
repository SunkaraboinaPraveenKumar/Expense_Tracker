import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.firstapp.expense_tracker.ExpenseRecord
import java.time.LocalDate

@Composable
fun AnalysisScreen(
    onBack: () -> Unit,
    expenseRecords: List<ExpenseRecord>,
    navigateToScreen: (Screen) -> Unit // Add navigateToScreen parameter
) {
    // Example data for the pie chart
    val incomeData = listOf("Salary" to 2000f, "Investment" to 1500f, "Others" to 500f)
    val expenseData = listOf("Food" to 300f, "Transportation" to 150f, "Entertainment" to 200f)

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text(text = "Income Analysis")
        CustomPieChart(data = incomeData, modifier = Modifier.weight(1f))

        Text(text = "Expense Analysis")
        CustomPieChart(data = expenseData, modifier = Modifier.weight(1f))

        Button(onClick = onBack) {
            Text(text = "Back")
        }
    }
}
