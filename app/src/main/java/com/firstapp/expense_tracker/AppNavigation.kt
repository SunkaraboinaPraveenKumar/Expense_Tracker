
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.Accounts.route) {
            AccountsScreen(navController = navController)
        }
        composable(Screen.Analysis.route) {
            AnalysisScreen(navController = navController)
        }
        composable(Screen.Debts.route) {
            DebtsScreen(navController = navController)
        }
    }
}

@Composable
fun HomeScreen(navController: NavController) {
    Column {
        // Other content of the HomeScreen

        Button(onClick = {
            navController.navigate(Screen.Home.route)
        }) {
            Text(text = "Home")
        }
    }
}

@Composable
fun AccountsScreen(navController: NavController) {
    Column {
        // Other content of the AccountsScreen

        Button(onClick = {
            navController.navigate(Screen.Accounts.route)
        }) {
            Text(text = "Accounts")
        }
    }
}

@Composable
fun AnalysisScreen(navController: NavController) {
    Column {
        // Other content of the AnalysisScreen

        Button(onClick = {
            navController.navigate(Screen.Analysis.route)
        }) {
            Text(text = "Analysis")
        }
    }
}

@Composable
fun DebtsScreen(navController: NavController) {
    Column {
        // Other content of the DebtsScreen

        Button(onClick = {
            navController.navigate(Screen.Debts.route)
        }) {
            Text(text = "Debts")
        }
    }
}
