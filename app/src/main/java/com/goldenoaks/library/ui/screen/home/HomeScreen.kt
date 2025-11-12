package com.goldenoaks.library.ui.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.goldenoaks.library.ui.theme.GoldenOaksLibraryTheme
import com.goldenoaks.library.ui.viewmodel.AuthViewModel

data class MenuItem(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val onClick: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToCatalog: () -> Unit,
    onNavigateToMembers: () -> Unit,
    onNavigateToTransactions: () -> Unit,
    onNavigateToFines: () -> Unit,
    onNavigateToReports: () -> Unit,
    onLogout: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authState by authViewModel.authState.collectAsState()
    val currentUser = authState.currentUser

    val menuItems = listOf(
        MenuItem("Catalog", Icons.Default.MenuBook, onNavigateToCatalog),
        MenuItem("Members", Icons.Default.People, onNavigateToMembers),
        MenuItem("Transactions", Icons.Default.SwapHoriz, onNavigateToTransactions),
        MenuItem("Fines", Icons.Default.AttachMoney, onNavigateToFines),
        MenuItem("Reports", Icons.Default.Assessment, onNavigateToReports)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Golden Oaks Library") },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Welcome, ${currentUser?.name ?: "User"}!",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "Role: ${currentUser?.role ?: "Unknown"}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(menuItems) { item ->
                    Card(
                        onClick = item.onClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    GoldenOaksLibraryTheme {
        HomeScreen(
            onNavigateToCatalog = {},
            onNavigateToMembers = {},
            onNavigateToTransactions = {},
            onNavigateToFines = {},
            onNavigateToReports = {},
            onLogout = {}
        )
    }
}

