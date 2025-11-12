package com.goldenoaks.library.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.goldenoaks.library.data.model.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM books WHERE bookId = :bookId LIMIT 1")
    suspend fun getBookById(bookId: Long): Book?

    @Query("SELECT * FROM books WHERE isbn = :isbn LIMIT 1")
    suspend fun getBookByIsbn(isbn: String): Book?

    @Query("""
        SELECT * FROM books 
        WHERE title LIKE '%' || :query || '%' 
        OR author LIKE '%' || :query || '%' 
        OR genre LIKE '%' || :query || '%'
        OR isbn LIKE '%' || :query || '%'
    """)
    fun searchBooks(query: String): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE genre = :genre")
    fun getBooksByGenre(genre: String): Flow<List<Book>>

    @Query("SELECT * FROM books")
    fun getAllBooks(): Flow<List<Book>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book): Long

    @Query("UPDATE books SET title = :title, author = :author, genre = :genre, publisher = :publisher, year = :year, description = :description WHERE bookId = :bookId")
    suspend fun updateBook(bookId: Long, title: String, author: String, genre: String, publisher: String?, year: Int?, description: String?)

    @Query("DELETE FROM books WHERE bookId = :bookId")
    suspend fun deleteBook(bookId: Long)
}

