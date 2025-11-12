package com.goldenoaks.library.ui.screen.reports

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.goldenoaks.library.ui.viewmodel.ReportViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    onNavigateBack: () -> Unit,
    viewModel: ReportViewModel = hiltViewModel()
) {
    val reportState by viewModel.reportState.collectAsState()
    var selectedReportType by remember { mutableStateOf<ReportType?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reports") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        selectedReportType = ReportType.ACTIVITY
                        viewModel.generateActivityReport()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Activity Report")
                }
                Button(
                    onClick = {
                        selectedReportType = ReportType.INVENTORY
                        viewModel.generateInventoryReport()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Inventory Report")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (reportState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                when (selectedReportType) {
                    ReportType.ACTIVITY -> {
                        reportState.activityReport?.let { report ->
                            ActivityReportContent(report = report)
                        }
                    }
                    ReportType.INVENTORY -> {
                        reportState.inventoryReport?.let { report ->
                            InventoryReportContent(report = report)
                        }
                    }
                    null -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Select a report type to generate")
                        }
                    }
                }
            }

            reportState.errorMessage?.let { error ->
                LaunchedEffect(error) {
                    viewModel.clearError()
                }
            }
        }
    }
}

@Composable
fun ActivityReportContent(report: com.goldenoaks.library.ui.viewmodel.ActivityReport) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Activity Report",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                ReportRow("Total Loans", report.totalLoans.toString())
                ReportRow("Active Loans", report.activeLoans.toString())
                ReportRow("Returned Loans", report.returnedLoans.toString())
                ReportRow("Overdue Loans", report.overdueLoans.toString())
            }
        }

        if (report.loansByDateRange.isNotEmpty()) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Loans by Date",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    report.loansByDateRange.forEach { (date, count) ->
                        ReportRow(date, count.toString())
                    }
                }
            }
        }
    }
}

@Composable
fun InventoryReportContent(report: com.goldenoaks.library.ui.viewmodel.InventoryReport) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Inventory Report",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                ReportRow("Total Books", report.totalBooks.toString())
                ReportRow("Total Copies", report.totalCopies.toString())
                ReportRow("Available Copies", report.availableCopies.toString())
                ReportRow("On Loan Copies", report.onLoanCopies.toString())
            }
        }

        if (report.booksByGenre.isNotEmpty()) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Books by Genre",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    report.booksByGenre.forEach { (genre, count) ->
                        ReportRow(genre, count.toString())
                    }
                }
            }
        }
    }
}

@Composable
fun ReportRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

enum class ReportType {
    ACTIVITY,
    INVENTORY
}

@Preview(showBackground = true)
@Composable
fun ReportsScreenPreview() {
    GoldenOaksLibraryTheme {
        ReportsScreen(onNavigateBack = {})
    }
}

