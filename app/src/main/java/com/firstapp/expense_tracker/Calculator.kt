package com.firstapp.expense_tracker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Calculator(onValueChange: (Double) -> Unit) {
    var displayValue by remember { mutableStateOf("0") }

    val buttons = listOf(
        listOf("C", "7", "8", "9", "/"),
        listOf("4", "5", "6", "*"),
        listOf("1", "2", "3", "-"),
        listOf("0", ".", "=", "+")
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = displayValue,
            fontSize = 32.sp,
            color = Color.Blue,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.End
        )
        buttons.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { button ->
                    CalculatorButton(buttonText = button) {
                        displayValue = handleButtonPress(displayValue, button)
                        if (button == "=") {
                            onValueChange(displayValue.toDoubleOrNull() ?: 0.0)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CalculatorButton(buttonText: String, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(8.dp)
            .size(64.dp)
            .background(Color.Gray, RoundedCornerShape(8.dp))
            .clickable { onClick() }
    ) {
        Text(text = buttonText, fontSize = 24.sp, color = Color.White)
    }
}

fun handleButtonPress(currentDisplay: String, button: String): String {
    return when (button) {
        "C" -> "0"
        "=" -> {
            try {
                val result = EvaluateExpression(currentDisplay)
                result.toString()
            } catch (e: Exception) {
                "Error"
            }
        }
        else -> {
            if (currentDisplay == "0" && button != ".") {
                button
            } else {
                currentDisplay + button
            }
        }
    }
}

