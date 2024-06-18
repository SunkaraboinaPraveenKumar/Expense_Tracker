package com.firstapp.expense_tracker
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun SettingsScreen() {
    var showDialog by remember { mutableStateOf(false) }
    var selectedUiMode by remember { mutableStateOf(UiMode.SystemDefault) }

    Column {
        // Top App Bar with title "Settings"
        TopAppBar(
            title = {
                Text(text = "Settings")
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Content for Settings screen
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Appearance (non-clickable, non-selectable text)
            Text(
                text = "Appearance",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // UI Mode option
            ListItem(
                modifier = Modifier.clickable(onClick = { showDialog = true }),
                text = {
                    Text(
                        text = "UI Mode",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            )

            // Dialog for selecting UI mode
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = {
                        Text(text = "UI Mode")
                    },
                    buttons = {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = { showDialog = false },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = MaterialTheme.colorScheme.surface
                                )
                            ) {
                                Text("Cancel")
                            }
                        }
                    },
                    text = {
                        Column {
                            RadioOption(
                                text = "Light",
                                selected = selectedUiMode == UiMode.Light,
                                onSelect = { selectedUiMode = UiMode.Light }
                            )
                            RadioOption(
                                text = "Dark",
                                selected = selectedUiMode == UiMode.Dark,
                                onSelect = { selectedUiMode = UiMode.Dark }
                            )
                            RadioOption(
                                text = "System Default",
                                selected = selectedUiMode == UiMode.SystemDefault,
                                onSelect = { selectedUiMode = UiMode.SystemDefault }
                            )
                        }
                    }
                )
            }
        }
    }

    // Effect to handle UI mode changes
    LaunchedEffect(selectedUiMode) {
        when (selectedUiMode) {
            UiMode.Light -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            UiMode.Dark -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            UiMode.SystemDefault -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
    }
}

@Composable
fun RadioOption(
    text: String,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelect
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text)
    }
}

enum class UiMode {
    Light, Dark, SystemDefault
}