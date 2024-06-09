package com.firstapp.expense_tracker
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource

@Composable
fun BottomNavBar(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
) {
    NavigationBar {
        val items = listOf(
            NavigationItem("Home", R.drawable.ic_home),
            NavigationItem("Records", R.drawable.ic_accounts),
            NavigationItem("Analysis", R.drawable.ic_analysis),
            NavigationItem("Budget", R.drawable.ic_debts)
        )
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(painterResource(id = item.iconRes), contentDescription = item.label) },
                label = { Text(item.label) },
                selected = selectedTabIndex == index,
                onClick = { onTabSelected(index) }
            )
        }
    }
}

data class NavigationItem(val label: String, val iconRes: Int)