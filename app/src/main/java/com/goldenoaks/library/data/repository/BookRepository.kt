package com.goldenoaks.library.data.repository

import com.goldenoaks.library.data.dao.BookDao
import com.goldenoaks.library.data.model.Book
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepository @Inject constructor(
    private val bookDao: BookDao
) {
    suspend fun getBookById(bookId: Long): Book? = bookDao.getBookById(bookId)

    suspend fun getBookByIsbn(isbn: String): Book? = bookDao.getBookByIsbn(isbn)

    fun searchBooks(query: String): Flow<List<Book>> = bookDao.searchBooks(query)

    fun getBooksByGenre(genre: String): Flow<List<Book>> = bookDao.getBooksByGenre(genre)

    fun getAllBooks(): Flow<List<Book>> = bookDao.getAllBooks()

    suspend fun insertBook(book: Book): Long = bookDao.insertBook(book)

    suspend fun updateBook(
        bookId: Long,
        title: String,
        author: String,
        genre: String,
        publisher: String?,
        year: Int?,
        description: String?
    ) = bookDao.updateBook(bookId, title, author, genre, publisher, year, description)

    suspend fun deleteBook(bookId: Long) = bookDao.deleteBook(bookId)
}

