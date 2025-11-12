package com.goldenoaks.library.util

import com.goldenoaks.library.data.model.*
import com.goldenoaks.library.data.repository.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

object DatabaseInitializer {
    fun initializeDatabase(
        userRepository: UserRepository,
        bookRepository: BookRepository,
        copyRepository: CopyRepository,
        memberRepository: MemberRepository
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            // Check if admin user exists
            val adminUser = userRepository.getUserByEmail("admin@golden-oaks.org")
            if (adminUser == null) {
                // Create default admin user
                val admin = User(
                    email = "admin@golden-oaks.org",
                    name = "System Administrator",
                    passwordHash = PasswordHasher.hashPassword("admin123"),
                    role = UserRole.ADMIN
                )
                userRepository.insertUser(admin)

                // Create default librarian
                val librarian = User(
                    email = "librarian@golden-oaks.org",
                    name = "Head Librarian",
                    passwordHash = PasswordHasher.hashPassword("librarian123"),
                    role = UserRole.LIBRARIAN
                )
                userRepository.insertUser(librarian)

                // Create default patron
                val patron = User(
                    email = "patron@golden-oaks.org",
                    name = "Test Patron",
                    passwordHash = PasswordHasher.hashPassword("patron123"),
                    role = UserRole.PATRON
                )
                userRepository.insertUser(patron)
            }

            // Check if books exist
            val existingBooks = bookRepository.getAllBooks().first()
            val hasBooks = existingBooks.isNotEmpty()

            if (!hasBooks) {
                // Add sample books
                val sampleBooks = listOf(
                    Book(
                        isbn = "978-0-123456-78-9",
                        title = "Introduction to Computer Science",
                        author = "John Smith",
                        genre = "Technology",
                        publisher = "Tech Publishers",
                        year = 2023,
                        description = "A comprehensive guide to computer science fundamentals."
                    ),
                    Book(
                        isbn = "978-0-987654-32-1",
                        title = "The Art of Programming",
                        author = "Jane Doe",
                        genre = "Technology",
                        publisher = "Code Books",
                        year = 2022,
                        description = "Learn programming from scratch."
                    ),
                    Book(
                        isbn = "978-0-555555-55-5",
                        title = "Database Design Principles",
                        author = "Robert Johnson",
                        genre = "Technology",
                        publisher = "Data Press",
                        year = 2024,
                        description = "Master database design and implementation."
                    )
                )

                sampleBooks.forEach { book ->
                    val bookId = bookRepository.insertBook(book)
                    // Add 2 copies of each book
                    for (i in 1..2) {
                        copyRepository.insertCopy(
                            Copy(
                                bookId = bookId,
                                barcode = "${book.isbn}-COPY-$i",
                                status = CopyStatus.AVAILABLE,
                                location = "Shelf A-$i"
                            )
                        )
                    }
                }

                // Add sample member
                memberRepository.insertMember(
                    Member(
                        name = "John Dlamini",
                        contactNumber = "+27 12 345 6789",
                        address = "123 Main Street, Pretoria",
                        email = "john.dlamini@example.com",
                        status = MemberStatus.ACTIVE
                    )
                )
            }
        }
    }
}

