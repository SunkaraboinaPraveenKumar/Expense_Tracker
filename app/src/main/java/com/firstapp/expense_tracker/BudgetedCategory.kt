import java.time.LocalDateTime
import java.time.YearMonth

data class BudgetedCategory(
    val category: String,
    val limit: Double,
    val spent: Double,
    val remaining: Double,
    val monthYear: YearMonth,
    val dateTime: LocalDateTime,
)
