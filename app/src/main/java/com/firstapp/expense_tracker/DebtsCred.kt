

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firstapp.expense_tracker.Credit
import com.firstapp.expense_tracker.Debt
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DebtsScreen(onBack:()->Unit) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) }
    val (debtTransactions, setDebtTransactions) = remember { mutableStateOf(listOf<Debt>()) }
    val (creditTransactions, setCreditTransactions) = remember { mutableStateOf(listOf<Credit>()) }
    val (allTransactions, setAllTransactions) = remember { mutableStateOf<List<Transaction>>(emptyList()) }
    val (showTransactionScreen, setShowTransactionScreen) = remember { mutableStateOf(false) }

    if (showTransactionScreen) {
        TransactionScreen(transactions = allTransactions) {
            setShowTransactionScreen(false)
        }
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text(text = "Debts/Credits") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_media_previous),
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
//                backgroundColor = Color.Blue
            )
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    TextButton(onClick = { setShowTransactionScreen(true) }) {
                        Text(text = "Transaction History", fontSize = 16.sp)
                    }
                    Row {
                        TabButton(
                            text = "Debt",
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 }
                        )
                        TabButton(
                            text = "Credit",
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                if (selectedTab == 0) {
                    DebtForm(onAddDebt = { debt ->
                        val updatedTransactions = debtTransactions.toMutableList()
                        updatedTransactions.add(debt)
                        setDebtTransactions(updatedTransactions)
                        updateAllTransactions(updatedTransactions, creditTransactions, setAllTransactions)
                    })
                } else {
                    CreditForm(onAddCredit = { credit ->
                        val updatedTransactions = creditTransactions.toMutableList()
                        updatedTransactions.add(credit)
                        setCreditTransactions(updatedTransactions)
                        updateAllTransactions(debtTransactions, updatedTransactions, setAllTransactions)
                    })
                }
            }
        }
    }
}

fun updateAllTransactions(
    debtTransactions: List<Debt>,
    creditTransactions: List<Credit>,
    setAllTransactions: (List<Transaction>) -> Unit
) {
    val allTransactions = (debtTransactions.map { transaction ->
        Transaction(
            amount = transaction.amount,
            toOrFrom = transaction.to,
            description = transaction.description,
            dateOfRepayment = transaction.dateOfRepayment,
            isDebt = true
        )
    } + creditTransactions.map { transaction ->
        Transaction(
            amount = transaction.amount,
            toOrFrom = transaction.from,
            description = transaction.description,
            dateOfRepayment = transaction.dateOfRepayment,
            isDebt = false
        )
    }).sortedBy { it.dateOfRepayment }

    setAllTransactions(allTransactions)
}

@Composable
fun TabButton(text: String, selected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            containerColor = if (selected) Color.Blue else Color.Gray
        ),
        modifier = Modifier.padding(4.dp)
    ) {
        Text(text = text)
    }
}

@Composable
fun TransactionScreen(transactions: List<Transaction>, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TextButton(onClick = onBack) {
            Text(text = "Back to Debt/Credit Screen")
        }
        Spacer(modifier = Modifier.height(16.dp))
        TransactionHistoryScreen(transactions = transactions)
    }
}

@Composable
fun TransactionHistoryScreen(transactions: List<Transaction>) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(transactions) { transaction ->
            TransactionRow(transaction)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun TransactionRow(transaction: Transaction) {
    val borderColor = if (transaction.isDebt) Color.Red else Color.Green
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text("Amount: ${transaction.amount}", color = borderColor)
        if (transaction.isDebt) {
            Text("To: ${transaction.toOrFrom}", color = borderColor)
        } else {
            Text("From: ${transaction.toOrFrom}", color = borderColor)
        }
        Text("Description: ${transaction.description}", color = borderColor)
        Text("Date of Repayment: ${transaction.dateOfRepayment}", color = borderColor)
    }
}

data class Transaction(
    val amount: Double,
    val toOrFrom: String,
    val description: String,
    val dateOfRepayment: LocalDate,
    val isDebt: Boolean
)