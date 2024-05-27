import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.firstapp.expense_tracker.Debt
import java.time.LocalDate
import java.time.format.DateTimeParseException

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DebtForm(onAddDebt: (Debt) -> Unit) {
    var amount by remember { mutableStateOf(TextFieldValue()) }
    var to by remember { mutableStateOf(TextFieldValue()) }
    var description by remember { mutableStateOf(TextFieldValue()) }
    var dateOfRepayment by remember { mutableStateOf(TextFieldValue()) }
    var dateError by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            enabled = true,
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
        OutlinedTextField(
            enabled = true,
            value = to,
            onValueChange = { to = it },
            label = { Text("To") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
        OutlinedTextField(
            enabled = true,
            value = dateOfRepayment,
            onValueChange = { dateOfRepayment = it },
            label = { Text("Date of Repayment (YYYY-MM-DD)") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            isError = dateError != null
        )
        if (dateError != null) {
            Text(text = dateError!!, color = Color.Red, modifier = Modifier.padding(horizontal = 16.dp))
        }
        OutlinedTextField(
            enabled = true,
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                try {
                    val parsedDate = LocalDate.parse(dateOfRepayment.text)
                    val debt = Debt(amount.text.toDouble(), to.text, description.text, parsedDate)
                    onAddDebt(debt)
                    dateError = null
                    amount = TextFieldValue("")
                    to = TextFieldValue("")
                    description = TextFieldValue("")
                    dateOfRepayment = TextFieldValue("")
                } catch (e: DateTimeParseException) {
                    dateError = "Invalid date format. Please use YYYY-MM-DD."
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Debt")
        }
    }
}
