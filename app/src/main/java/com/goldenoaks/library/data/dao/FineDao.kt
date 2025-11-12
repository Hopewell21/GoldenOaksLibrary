package com.goldenoaks.library.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.goldenoaks.library.data.model.Fine
import com.goldenoaks.library.data.model.FineStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface FineDao {
    @Query("SELECT * FROM fines WHERE fineId = :fineId LIMIT 1")
    suspend fun getFineById(fineId: Long): Fine?

    @Query("SELECT * FROM fines WHERE loanId = :loanId")
    fun getFinesByLoanId(loanId: Long): Flow<List<Fine>>

    @Query("""
        SELECT f.* FROM fines f
        INNER JOIN loans l ON f.loanId = l.loanId
        WHERE l.memberId = :memberId
    """)
    fun getFinesByMemberId(memberId: Long): Flow<List<Fine>>

    @Query("""
        SELECT f.* FROM fines f
        INNER JOIN loans l ON f.loanId = l.loanId
        WHERE l.memberId = :memberId AND f.status = :status
    """)
    fun getFinesByMemberIdAndStatus(memberId: Long, status: FineStatus): Flow<List<Fine>>

    @Query("SELECT * FROM fines WHERE status = :status")
    fun getFinesByStatus(status: FineStatus): Flow<List<Fine>>

    @Query("SELECT * FROM fines")
    fun getAllFines(): Flow<List<Fine>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFine(fine: Fine): Long

    @Query("UPDATE fines SET status = :status, paidDate = :paidDate WHERE fineId = :fineId")
    suspend fun updateFineStatus(fineId: Long, status: FineStatus, paidDate: Long?)

    @Query("DELETE FROM fines WHERE fineId = :fineId")
    suspend fun deleteFine(fineId: Long)
}

