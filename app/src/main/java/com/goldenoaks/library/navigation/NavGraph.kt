package com.goldenoaks.library.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.goldenoaks.library.ui.screen.auth.LoginScreen
import com.goldenoaks.library.ui.screen.catalog.CatalogScreen
import com.goldenoaks.library.ui.screen.fines.FinesScreen
import com.goldenoaks.library.ui.screen.home.HomeScreen
import com.goldenoaks.library.ui.screen.members.MembersScreen
import com.goldenoaks.library.ui.screen.reports.ReportsScreen
import com.goldenoaks.library.ui.screen.transactions.TransactionsScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object Catalog : Screen("catalog")
    object Members : Screen("members")
    object Transactions : Screen("transactions")
    object Fines : Screen("fines")
    object Reports : Screen("reports")
}

@Composable
fun NavGraph(navController: NavHostController, startDestination: String = Screen.Login.route) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToCatalog = { navController.navigate(Screen.Catalog.route) },
                onNavigateToMembers = { navController.navigate(Screen.Members.route) },
                onNavigateToTransactions = { navController.navigate(Screen.Transactions.route) },
                onNavigateToFines = { navController.navigate(Screen.Fines.route) },
                onNavigateToReports = { navController.navigate(Screen.Reports.route) },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Catalog.route) {
            CatalogScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Members.route) {
            MembersScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Transactions.route) {
            TransactionsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Fines.route) {
            FinesScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Reports.route) {
            ReportsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

