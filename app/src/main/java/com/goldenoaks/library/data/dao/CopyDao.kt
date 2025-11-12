package com.goldenoaks.library.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.goldenoaks.library.data.model.Copy
import com.goldenoaks.library.data.model.CopyStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface CopyDao {
    @Query("SELECT * FROM copies WHERE copyId = :copyId LIMIT 1")
    suspend fun getCopyById(copyId: Long): Copy?

    @Query("SELECT * FROM copies WHERE barcode = :barcode LIMIT 1")
    suspend fun getCopyByBarcode(barcode: String): Copy?

    @Query("SELECT * FROM copies WHERE bookId = :bookId")
    fun getCopiesByBookId(bookId: Long): Flow<List<Copy>>

    @Query("SELECT * FROM copies WHERE bookId = :bookId AND status = :status")
    fun getCopiesByBookIdAndStatus(bookId: Long, status: CopyStatus): Flow<List<Copy>>

    @Query("SELECT * FROM copies WHERE status = :status")
    fun getCopiesByStatus(status: CopyStatus): Flow<List<Copy>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCopy(copy: Copy): Long

    @Query("UPDATE copies SET status = :status, location = :location WHERE copyId = :copyId")
    suspend fun updateCopyStatus(copyId: Long, status: CopyStatus, location: String?)

    @Query("DELETE FROM copies WHERE copyId = :copyId")
    suspend fun deleteCopy(copyId: Long)
}

