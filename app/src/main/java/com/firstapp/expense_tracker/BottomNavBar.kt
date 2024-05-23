package com.firstapp.expense_tracker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BottomNavBar(selectedTabIndex: Int, onTabSelected: (Int) -> Unit) {
    val tabs = listOf("Home", "Accounts", "Analysis", "Debts")
    val icons = listOf(
        R.drawable.ic_home,      // Replace with your actual icon resources
        R.drawable.ic_accounts,  // Replace with your actual icon resources
        R.drawable.ic_analysis,  // Replace with your actual icon resources
        R.drawable.ic_debts      // Replace with your actual icon resources
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFF5722))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        tabs.forEachIndexed { index, title ->
            val isSelected = selectedTabIndex == index
            val color = if (isSelected) Color.White else Color.LightGray

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                    .clickable { onTabSelected(index) }
            ) {
                Icon(
                    painter = painterResource(id = icons[index]),
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = title,
                    color = color,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}
