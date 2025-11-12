package com.goldenoaks.library.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goldenoaks.library.data.model.Book
import com.goldenoaks.library.data.model.Copy
import com.goldenoaks.library.data.model.CopyStatus
import com.goldenoaks.library.data.repository.BookRepository
import com.goldenoaks.library.data.repository.CopyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BookWithAvailability(
    val book: Book,
    val totalCopies: Int,
    val availableCopies: Int
)

data class CatalogState(
    val books: List<BookWithAvailability> = emptyList(),
    val searchQuery: String = "",
    val selectedGenre: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val copyRepository: CopyRepository
) : ViewModel() {

    private val _catalogState = MutableStateFlow(CatalogState())
    val catalogState: StateFlow<CatalogState> = _catalogState.asStateFlow()

    init {
        loadAllBooks()
    }

    fun searchBooks(query: String) {
        _catalogState.value = _catalogState.value.copy(searchQuery = query)
        viewModelScope.launch {
            _catalogState.value = _catalogState.value.copy(isLoading = true)
            try {
                val booksFlow = if (query.isBlank()) {
                    bookRepository.getAllBooks()
                } else {
                    bookRepository.searchBooks(query)
                }

                booksFlow.collect { bookList ->
                    val booksWithAvailability = bookList.map { book ->
                        val copyList = copyRepository.getCopiesByBookId(book.bookId).first()
                        val totalCopies = copyList.size
                        val availableCopies = copyList.count { it.status == CopyStatus.AVAILABLE }
                        BookWithAvailability(book, totalCopies, availableCopies)
                    }

                    _catalogState.value = _catalogState.value.copy(
                        books = booksWithAvailability,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _catalogState.value = _catalogState.value.copy(
                    isLoading = false,
                    errorMessage = "Search failed: ${e.message}"
                )
            }
        }
    }

    private fun loadAllBooks() {
        viewModelScope.launch {
            _catalogState.value = _catalogState.value.copy(isLoading = true)
            try {
                bookRepository.getAllBooks().collect { bookList ->
                    val booksWithAvailability = bookList.map { book ->
                        val copyList = copyRepository.getCopiesByBookId(book.bookId).first()
                        val totalCopies = copyList.size
                        val availableCopies = copyList.count { it.status == CopyStatus.AVAILABLE }
                        BookWithAvailability(book, totalCopies, availableCopies)
                    }
                    _catalogState.value = _catalogState.value.copy(
                        books = booksWithAvailability,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _catalogState.value = _catalogState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to load books: ${e.message}"
                )
            }
        }
    }

    fun addBook(
        isbn: String,
        title: String,
        author: String,
        genre: String,
        publisher: String?,
        year: Int?,
        description: String?
    ) {
        viewModelScope.launch {
            try {
                val book = Book(
                    isbn = isbn,
                    title = title,
                    author = author,
                    genre = genre,
                    publisher = publisher,
                    year = year,
                    description = description
                )
                bookRepository.insertBook(book)
                loadAllBooks()
            } catch (e: Exception) {
                _catalogState.value = _catalogState.value.copy(
                    errorMessage = "Failed to add book: ${e.message}"
                )
            }
        }
    }

    fun updateBook(
        bookId: Long,
        title: String,
        author: String,
        genre: String,
        publisher: String?,
        year: Int?,
        description: String?
    ) {
        viewModelScope.launch {
            try {
                bookRepository.updateBook(bookId, title, author, genre, publisher, year, description)
                loadAllBooks()
            } catch (e: Exception) {
                _catalogState.value = _catalogState.value.copy(
                    errorMessage = "Failed to update book: ${e.message}"
                )
            }
        }
    }

    fun deleteBook(bookId: Long) {
        viewModelScope.launch {
            try {
                bookRepository.deleteBook(bookId)
                loadAllBooks()
            } catch (e: Exception) {
                _catalogState.value = _catalogState.value.copy(
                    errorMessage = "Failed to delete book: ${e.message}"
                )
            }
        }
    }

    fun clearError() {
        _catalogState.value = _catalogState.value.copy(errorMessage = null)
    }
}

