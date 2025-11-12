package com.goldenoaks.library.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.goldenoaks.library.data.model.Loan
import kotlinx.coroutines.flow.Flow

@Dao
interface LoanDao {
    @Query("SELECT * FROM loans WHERE loanId = :loanId LIMIT 1")
    suspend fun getLoanById(loanId: Long): Loan?

    @Query("SELECT * FROM loans WHERE memberId = :memberId AND returnDate IS NULL")
    fun getActiveLoansByMember(memberId: Long): Flow<List<Loan>>

    @Query("SELECT * FROM loans WHERE memberId = :memberId")
    fun getAllLoansByMember(memberId: Long): Flow<List<Loan>>

    @Query("SELECT * FROM loans WHERE copyId = :copyId AND returnDate IS NULL LIMIT 1")
    suspend fun getActiveLoanByCopy(copyId: Long): Loan?

    @Query("SELECT * FROM loans WHERE returnDate IS NULL AND dueDate < :currentTime")
    fun getOverdueLoans(currentTime: Long = System.currentTimeMillis()): Flow<List<Loan>>

    @Query("SELECT * FROM loans WHERE returnDate IS NULL")
    fun getAllActiveLoans(): Flow<List<Loan>>

    @Query("SELECT * FROM loans")
    fun getAllLoans(): Flow<List<Loan>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLoan(loan: Loan): Long

    @Query("UPDATE loans SET returnDate = :returnDate WHERE loanId = :loanId")
    suspend fun returnLoan(loanId: Long, returnDate: Long)

    @Query("DELETE FROM loans WHERE loanId = :loanId")
    suspend fun deleteLoan(loanId: Long)
}

