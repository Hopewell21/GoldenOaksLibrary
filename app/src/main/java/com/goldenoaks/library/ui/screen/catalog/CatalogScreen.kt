package com.goldenoaks.library.ui.screen.catalog

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
import com.goldenoaks.library.ui.viewmodel.CatalogViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    onNavigateBack: () -> Unit,
    viewModel: CatalogViewModel = hiltViewModel()
) {
    val catalogState by viewModel.catalogState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var showAddBookDialog by remember { mutableStateOf(false) }

    LaunchedEffect(searchQuery) {
        viewModel.searchBooks(searchQuery)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catalog") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showAddBookDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Book")
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
                label = { Text("Search books...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                singleLine = true
            )

            if (catalogState.isLoading) {
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
                    items(catalogState.books) { bookWithAvailability ->
                        BookCard(
                            book = bookWithAvailability.book,
                            availableCopies = bookWithAvailability.availableCopies,
                            totalCopies = bookWithAvailability.totalCopies
                        )
                    }
                }
            }

            catalogState.errorMessage?.let { error ->
                LaunchedEffect(error) {
                    viewModel.clearError()
                }
            }
        }
    }

    if (showAddBookDialog) {
        AddBookDialog(
            onDismiss = { showAddBookDialog = false },
            onAddBook = { isbn, title, author, genre, publisher, year, description ->
                viewModel.addBook(isbn, title, author, genre, publisher, year, description)
                showAddBookDialog = false
            }
        )
    }
}

@Composable
fun BookCard(
    book: com.goldenoaks.library.data.model.Book,
    availableCopies: Int,
    totalCopies: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = book.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Author: ${book.author}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Genre: ${book.genre}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "ISBN: ${book.isbn}",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Available: $availableCopies/$totalCopies",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (availableCopies > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun AddBookDialog(
    onDismiss: () -> Unit,
    onAddBook: (String, String, String, String, String?, Int?, String?) -> Unit
) {
    var isbn by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var publisher by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Book") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = isbn,
                    onValueChange = { isbn = it },
                    label = { Text("ISBN *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = author,
                    onValueChange = { author = it },
                    label = { Text("Author *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = genre,
                    onValueChange = { genre = it },
                    label = { Text("Genre *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = publisher,
                    onValueChange = { publisher = it },
                    label = { Text("Publisher") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = year,
                    onValueChange = { year = it },
                    label = { Text("Year") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (isbn.isNotBlank() && title.isNotBlank() && author.isNotBlank() && genre.isNotBlank()) {
                        onAddBook(
                            isbn,
                            title,
                            author,
                            genre,
                            publisher.takeIf { it.isNotBlank() },
                            year.toIntOrNull(),
                            description.takeIf { it.isNotBlank() }
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
fun CatalogScreenPreview() {
    GoldenOaksLibraryTheme {
        CatalogScreen(onNavigateBack = {})
    }
}

