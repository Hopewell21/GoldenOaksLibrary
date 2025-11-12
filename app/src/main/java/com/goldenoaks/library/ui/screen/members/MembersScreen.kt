package com.goldenoaks.library.ui.screen.members

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
import com.goldenoaks.library.data.model.MemberStatus
import com.goldenoaks.library.ui.theme.GoldenOaksLibraryTheme
import com.goldenoaks.library.ui.viewmodel.MemberViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MembersScreen(
    onNavigateBack: () -> Unit,
    viewModel: MemberViewModel = hiltViewModel()
) {
    val memberState by viewModel.memberState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var showAddMemberDialog by remember { mutableStateOf(false) }

    LaunchedEffect(searchQuery) {
        viewModel.searchMembers(searchQuery)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Members") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showAddMemberDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Member")
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
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search members...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                singleLine = true
            )

            if (memberState.isLoading) {
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
                    items(memberState.members) { member ->
                        MemberCard(
                            member = member,
                            onUpdate = { /* TODO: Show update dialog */ },
                            onDelete = { viewModel.deleteMember(member.memberId) }
                        )
                    }
                }
            }

            memberState.errorMessage?.let { error ->
                LaunchedEffect(error) {
                    viewModel.clearError()
                }
            }
        }
    }

    if (showAddMemberDialog) {
        AddMemberDialog(
            onDismiss = { showAddMemberDialog = false },
            onAddMember = { name, contactNumber, address, email ->
                viewModel.addMember(name, contactNumber, address, email)
                showAddMemberDialog = false
            }
        )
    }
}

@Composable
fun MemberCard(
    member: com.goldenoaks.library.data.model.Member,
    onUpdate: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
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
                        text = member.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    member.email?.let {
                        Text(
                            text = "Email: $it",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    member.contactNumber?.let {
                        Text(
                            text = "Contact: $it",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Text(
                        text = "Status: ${member.status.name}",
                        style = MaterialTheme.typography.bodySmall,
                        color = when (member.status) {
                            MemberStatus.ACTIVE -> MaterialTheme.colorScheme.primary
                            MemberStatus.INACTIVE -> MaterialTheme.colorScheme.secondary
                            MemberStatus.SUSPENDED -> MaterialTheme.colorScheme.error
                        }
                    )
                }
                Row {
                    IconButton(onClick = onUpdate) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            }
        }
    }
}

@Composable
fun AddMemberDialog(
    onDismiss: () -> Unit,
    onAddMember: (String, String?, String?, String?) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Member") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = contactNumber,
                    onValueChange = { contactNumber = it },
                    label = { Text("Contact Number") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Address") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank()) {
                        onAddMember(
                            name,
                            contactNumber.takeIf { it.isNotBlank() },
                            address.takeIf { it.isNotBlank() },
                            email.takeIf { it.isNotBlank() }
                        )
                    }
                }
            ) {
                Text("Add")
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
fun MembersScreenPreview() {
    GoldenOaksLibraryTheme {
        MembersScreen(onNavigateBack = {})
    }
}

