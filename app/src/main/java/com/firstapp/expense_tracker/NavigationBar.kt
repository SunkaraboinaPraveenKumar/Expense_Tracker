import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firstapp.expense_tracker.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsMenu(
    onSettingsClick: () -> Unit,
    onDeleteResetClick: () -> Unit,
    onHelpClick: () -> Unit,
    onAddNewCategoriesClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(text = "Finance Management App",
            fontSize = 20.sp,
            modifier = Modifier.clickable {  }
        )
    }
    Spacer(modifier = Modifier.padding(8.dp))
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
        .background(color = androidx.compose.ui.graphics.Color.Black)
    )
    Spacer(modifier = Modifier.padding(8.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(8.dp)
    ) {
        IconButton(onClick = onSettingsClick) {
            Icon(
                painter = painterResource(id = R.drawable.settings),
                contentDescription = "Settings",
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Settings",modifier = Modifier.clickable { onSettingsClick })
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(8.dp)
    ) {
        IconButton(onClick = onDeleteResetClick) {
            Icon(
                painter = painterResource(id = R.drawable.delete),
                contentDescription = "Delete and Reset",
                modifier = Modifier.size(24.dp)
            )
        }
        Text(text = "Delete",modifier = Modifier.clickable { onDeleteResetClick })
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(8.dp)
    ){
        IconButton(onClick = onHelpClick) {
            Icon(
                painter = painterResource(id = R.drawable.question),
                contentDescription = "Help",
                modifier = Modifier.size(24.dp)
            )
        }
        Text(text = "Help",modifier = Modifier.clickable { onHelpClick })
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(8.dp)
    ) {
        IconButton(onClick = onAddNewCategoriesClick) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = "Add New Categories",
                modifier = Modifier.size(24.dp)
            )
        }
        Text(text = "Add New Categories",modifier = Modifier.clickable { onAddNewCategoriesClick })
    }
}