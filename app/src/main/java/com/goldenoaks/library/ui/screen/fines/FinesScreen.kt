package com.goldenoaks.library.ui.screen.fines

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.goldenoaks.library.data.model.FineStatus
import com.goldenoaks.library.ui.theme.GoldenOaksLibraryTheme
import com.goldenoaks.library.ui.viewmodel.FineViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinesScreen(
    onNavigateBack: () -> Unit,
    viewModel: FineViewModel = hiltViewModel()
) {
    val fineState by viewModel.fineState.collectAsState()
    var memberId by remember { mutableStateOf("") }
    var showMemberSearch by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadPendingFines()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fines") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showMemberSearch = true }) {
                        Icon(Icons.Default.Search, contentDescription = "Search by Member")
                    }
                    IconButton(onClick = { viewModel.calculateAndAssessFines() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Calculate Fines")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (fineState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(fineState.fines) { fine ->
                        FineCard(
                            fine = fine,
                            onPay = { viewModel.payFine(fine.fineId) },
                            onWaive = { viewModel.waiveFine(fine.fineId) }
                        )
                    }
                }
            }

            fineState.errorMessage?.let { error ->
                LaunchedEffect(error) {
                    viewModel.clearError()
                }
            }
        }
    }

    if (showMemberSearch) {
        AlertDialog(
            onDismissRequest = { showMemberSearch = false },
            title = { Text("Search Fines by Member") },
            text = {
                OutlinedTextField(
                    value = memberId,
                    onValueChange = { memberId = it },
                    label = { Text("Member ID") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val id = memberId.toLongOrNull()
                        if (id != null) {
                            viewModel.loadFinesByMember(id)
                            showMemberSearch = false
                        }
                    }
                ) {
                    Text("Search")
                }
            },
            dismissButton = {
                TextButton(onClick = { showMemberSearch = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun FineCard(
    fine: com.goldenoaks.library.data.model.Fine,
    onPay: () -> Unit,
    onWaive: () -> Unit
) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when (fine.status) {
                FineStatus.PENDING -> MaterialTheme.colorScheme.errorContainer
                FineStatus.PAID -> MaterialTheme.colorScheme.primaryContainer
                FineStatus.WAIVED -> MaterialTheme.colorScheme.secondaryContainer
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Fine ID: ${fine.fineId}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Loan ID: ${fine.loanId}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Amount: R${String.format("%.2f", fine.amount)}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Status: ${fine.status.name}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Assessed: ${dateFormat.format(Date(fine.assessedDate))}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    fine.paidDate?.let {
                        Text(
                            text = "Paid: ${dateFormat.format(Date(it))}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                if (fine.status == FineStatus.PENDING) {
                    Column {
                        Button(onClick = onPay, modifier = Modifier.padding(bottom = 4.dp)) {
                            Text("Pay")
                        }
                        OutlinedButton(onClick = onWaive) {
                            Text("Waive")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FinesScreenPreview() {
    GoldenOaksLibraryTheme {
        FinesScreen(onNavigateBack = {})
    }
}

