import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.firstapp.expense_tracker.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationBar(
    onSettingsClick: () -> Unit,
    onDeleteResetClick: () -> Unit,
    onHelpClick: () -> Unit,
    onAddNewCategoriesClick: () -> Unit
) {
    var isMenuVisible by remember { mutableStateOf(false) }
    var isSearchVisible by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Row(modifier = Modifier.fillMaxWidth()) {
                // Three vertical bars symbol to toggle menu visibility
                IconButton(onClick = { isMenuVisible = !isMenuVisible }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_menu),
                        contentDescription = "Menu",
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                // Search icon to toggle search visibility
                IconButton(onClick = { isSearchVisible = !isSearchVisible }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "Search",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        },
        actions = {
            if (isMenuVisible) {
                // Show the menu if the menu icon is clicked
                SettingsMenu(
                    onSettingsClick = onSettingsClick,
                    onDeleteResetClick = onDeleteResetClick,
                    onHelpClick = onHelpClick,
                    onAddNewCategoriesClick = onAddNewCategoriesClick
                )
            }
            if (isSearchVisible) {
                // Show the search popup if the search icon is clicked
                SearchPopup(onClose = { isSearchVisible = false })
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsMenu(
    onSettingsClick: () -> Unit,
    onDeleteResetClick: () -> Unit,
    onHelpClick: () -> Unit,
    onAddNewCategoriesClick: () -> Unit
) {
    IconButton(onClick = onSettingsClick) {
        Icon(
            painter = painterResource(id = R.drawable.settings),
            contentDescription = "Settings",
            modifier = Modifier.size(24.dp)
        )
    }
    IconButton(onClick = onDeleteResetClick) {
        Icon(
            painter = painterResource(id = R.drawable.delete),
            contentDescription = "Delete and Reset",
            modifier = Modifier.size(24.dp)
        )
    }
    IconButton(onClick = onHelpClick) {
        Icon(
            painter = painterResource(id = R.drawable.question),
            contentDescription = "Help",
            modifier = Modifier.size(24.dp)
        )
    }
    IconButton(onClick = onAddNewCategoriesClick) {
        Icon(
            painter = painterResource(id = R.drawable.ic_add),
            contentDescription = "Add New Categories",
            modifier = Modifier.size(24.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPopup(onClose: () -> Unit) {
    Popup(onDismissRequest = onClose) {
        // Your search component goes here
        Text("Search Component")
    }
}