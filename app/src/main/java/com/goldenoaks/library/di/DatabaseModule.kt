package com.goldenoaks.library.di

import android.content.Context
import androidx.room.Room
import com.goldenoaks.library.data.dao.BookDao
import com.goldenoaks.library.data.dao.CopyDao
import com.goldenoaks.library.data.dao.FineDao
import com.goldenoaks.library.data.dao.LoanDao
import com.goldenoaks.library.data.dao.MemberDao
import com.goldenoaks.library.data.dao.UserDao
import com.goldenoaks.library.data.database.LibraryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): LibraryDatabase {
        return Room.databaseBuilder(
            context,
            LibraryDatabase::class.java,
            "library_database"
        ).build()
    }

    @Provides
    fun provideUserDao(database: LibraryDatabase): UserDao = database.userDao()

    @Provides
    fun provideMemberDao(database: LibraryDatabase): MemberDao = database.memberDao()

    @Provides
    fun provideBookDao(database: LibraryDatabase): BookDao = database.bookDao()

    @Provides
    fun provideCopyDao(database: LibraryDatabase): CopyDao = database.copyDao()

    @Provides
    fun provideLoanDao(database: LibraryDatabase): LoanDao = database.loanDao()

    @Provides
    fun provideFineDao(database: LibraryDatabase): FineDao = database.fineDao()
}

