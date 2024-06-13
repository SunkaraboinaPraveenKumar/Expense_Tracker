package com.firstapp.expense_tracker

import SettingsMenu
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainLayout(
    navController: NavController,
    onViewFilterClick: () -> Unit,
    content: @Composable () -> Unit
) {
    var selectedBottomTabIndex by remember { mutableStateOf(0) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    // Track the current route to handle navigation state
    var currentRoute by remember { mutableStateOf(navController.currentDestination?.route) }

    // Listen to navigation changes and update the selected tab index
    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            currentRoute = destination.route
            selectedBottomTabIndex = when (destination.route) {
                "expenseTracker" -> 0
                "viewRecords" -> 1
                "analysis" -> 2
                "setBudget" -> 3
                else -> selectedBottomTabIndex
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.75f)
                    .background(Color.LightGray)
                    .padding(16.dp)
            ) {
                SettingsMenu(
                    onSettingsClick = { /* Handle Settings Click */ },
                    onDeleteResetClick = { /* Handle Delete/Reset Click */ },
                    onHelpClick = { /* Handle Help Click */ },
                    onAddNewCategoriesClick = { /* Handle Add New Categories Click */ }
                )
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                TopAppBar(
                    title = {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            IconButton(onClick = {
                                coroutineScope.launch {
                                    if (drawerState.isOpen) drawerState.close()
                                    else drawerState.open()
                                }
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_menu),
                                    contentDescription = "Menu",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(onClick = onViewFilterClick) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_search),
                                    contentDescription = "Search",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    },
                    actions = { /* Other actions can be added here */ }
                )

                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(tween(300)), // Add a custom fade-in animation
                    exit = fadeOut(tween(300)), // Add a custom fade-out animation
                    modifier = Modifier.weight(1f) // Allow content to take up remaining space
                ) {
                    content() // Insert screen-specific content here
                }

                BottomNavBar(
                    selectedTabIndex = selectedBottomTabIndex,
                    onTabSelected = { index ->
                        if (selectedBottomTabIndex != index) { // Only navigate if different
                            selectedBottomTabIndex = index
                            val newRoute = when (index) {
                                0 -> "expenseTracker"
                                1 -> "viewRecords"
                                2 -> "analysis"
                                3 -> "setBudget"
                                else -> return@BottomNavBar
                            }
                            // Navigate only if the route is different from the current one
                            if (currentRoute != newRoute) {
                                navController.navigate(newRoute) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}
