package com.goldenoaks.library.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.goldenoaks.library.data.dao.BookDao
import com.goldenoaks.library.data.dao.CopyDao
import com.goldenoaks.library.data.dao.FineDao
import com.goldenoaks.library.data.dao.LoanDao
import com.goldenoaks.library.data.dao.MemberDao
import com.goldenoaks.library.data.dao.UserDao
import com.goldenoaks.library.data.model.Book
import com.goldenoaks.library.data.model.Copy
import com.goldenoaks.library.data.model.Fine
import com.goldenoaks.library.data.model.Loan
import com.goldenoaks.library.data.model.Member
import com.goldenoaks.library.data.model.User

@Database(
    entities = [User::class, Member::class, Book::class, Copy::class, Loan::class, Fine::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class LibraryDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun memberDao(): MemberDao
    abstract fun bookDao(): BookDao
    abstract fun copyDao(): CopyDao
    abstract fun loanDao(): LoanDao
    abstract fun fineDao(): FineDao
}

