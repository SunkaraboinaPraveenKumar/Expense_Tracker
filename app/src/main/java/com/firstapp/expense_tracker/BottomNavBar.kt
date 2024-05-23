package com.firstapp.expense_tracker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


@Composable
fun BottomNavBar(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
) {
    val tabs = listOf(Screen.Home, Screen.Accounts, Screen.Analysis, Screen.Debts)
    val icons = listOf(
        R.drawable.ic_home,
        R.drawable.ic_accounts,
        R.drawable.ic_analysis,
        R.drawable.ic_debts
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFF5722))
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                tabs.forEachIndexed { index, screen ->
                    // Your existing code for handling tab selection
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                            .clickable { onTabSelected(index) }

                    ) {
                        Icon(
                            painter = painterResource(id = icons[index]),
                            contentDescription = screen.toString(),
                            tint = color,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
        }
    }
