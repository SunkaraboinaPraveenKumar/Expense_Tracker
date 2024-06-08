import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.firstapp.expense_tracker.Debt
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DebtForm(onAddDebt: (Debt) -> Unit) {
    var amount by remember { mutableStateOf(TextFieldValue()) }
    var to by remember { mutableStateOf(TextFieldValue()) }
    var description by remember { mutableStateOf(TextFieldValue()) }
    var dateOfRepayment by remember { mutableStateOf(TextFieldValue()) }
    var dateError by remember { mutableStateOf<String?>(null) }
    var amountError by remember { mutableStateOf<String?>(null) }
    var fromError by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                dateOfRepayment = TextFieldValue(selectedDate.format(dateFormatter))
                dateError = null
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
    val customTextFieldColors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        disabledTextColor = Color.Black,
        disabledBorderColor = Color.Black,
        disabledLabelColor = Color.Gray,
        cursorColor = Color.Black, // Not needed since readOnly, but useful for reference
        focusedBorderColor = Color.Black,
        unfocusedBorderColor = Color.Black,
        errorBorderColor = Color.Red
    )
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        if (amountError != null) {
            Text(
                text = amountError!!,
                color = Color.Red,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        OutlinedTextField(
            value = to,
            onValueChange = { to = it },
            label = { Text("To") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        if (fromError != null) {
            Text(
                text = fromError!!,
                color = Color.Red,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        OutlinedTextField(
            value = dateOfRepayment,
            onValueChange = { dateOfRepayment = it },
            label = { Text("Date of Repayment (YYYY-MM-DD)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clickable { showDatePicker() },
            enabled = false,
            singleLine = true,
            readOnly = true,
            isError = dateError != null,
            colors = customTextFieldColors
        )
        if (dateError != null) {
            Text(
                text = dateError!!,
                color = Color.Red,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                // Validate amount field
                if (amount.text.isEmpty()) {
                    amountError = "Amount cannot be empty"
                } else {
                    amountError = null
                }
                // Validate from field
                if (to.text.isEmpty()) {
                    fromError = "To cannot be empty"
                } else {
                    fromError = null
                }
                if (amountError == null && fromError == null) {
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
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Debt")
        }
    }
}