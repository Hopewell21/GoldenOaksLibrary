package com.goldenoaks.library.ui.screen.transactions

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
import com.goldenoaks.library.ui.theme.GoldenOaksLibraryTheme
import com.goldenoaks.library.ui.viewmodel.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    onNavigateBack: () -> Unit,
    viewModel: TransactionViewModel = hiltViewModel()
) {
    val transactionState by viewModel.transactionState.collectAsState()
    var showIssueDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transactions") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showIssueDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Issue Book")
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
            if (transactionState.isLoading) {
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
                    items(transactionState.activeLoans) { loan ->
                        LoanCard(
                            loan = loan,
                            onReturn = { viewModel.returnBook(loan.loanId) }
                        )
                    }
                }
            }

            transactionState.errorMessage?.let { error ->
                LaunchedEffect(error) {
                    viewModel.clearError()
                }
            }
        }
    }

    if (showIssueDialog) {
        IssueBookDialog(
            onDismiss = { showIssueDialog = false },
            onIssue = { memberId, copyId ->
                viewModel.issueBook(memberId, copyId)
                showIssueDialog = false
            }
        )
    }
}

@Composable
fun LoanCard(
    loan: com.goldenoaks.library.data.model.Loan,
    onReturn: () -> Unit
) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val isOverdue = loan.returnDate == null && loan.dueDate < System.currentTimeMillis()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isOverdue) MaterialTheme.colorScheme.errorContainer
            else MaterialTheme.colorScheme.surface
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
                        text = "Loan ID: ${loan.loanId}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Member ID: ${loan.memberId}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Copy ID: ${loan.copyId}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Issue Date: ${dateFormat.format(Date(loan.issueDate))}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "Due Date: ${dateFormat.format(Date(loan.dueDate))}",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isOverdue) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                    )
                    if (loan.returnDate != null) {
                        Text(
                            text = "Return Date: ${dateFormat.format(Date(loan.returnDate))}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                if (loan.returnDate == null) {
                    Button(onClick = onReturn) {
                        Text("Return")
                    }
                } else {
                    Text(
                        text = "Returned",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun IssueBookDialog(
    onDismiss: () -> Unit,
    onIssue: (Long, Long) -> Unit
) {
    var memberId by remember { mutableStateOf("") }
    var copyId by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Issue Book") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = memberId,
                    onValueChange = { memberId = it },
                    label = { Text("Member ID *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = copyId,
                    onValueChange = { copyId = it },
                    label = { Text("Copy ID *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val mId = memberId.toLongOrNull()
                    val cId = copyId.toLongOrNull()
                    if (mId != null && cId != null) {
                        onIssue(mId, cId)
                    }
                }
            ) {
                Text("Issue")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun TransactionsScreenPreview() {
    GoldenOaksLibraryTheme {
        TransactionsScreen(onNavigateBack = {})
    }
}

