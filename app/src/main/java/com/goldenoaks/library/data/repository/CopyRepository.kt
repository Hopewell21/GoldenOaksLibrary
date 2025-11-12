package com.goldenoaks.library.data.repository

import com.goldenoaks.library.data.dao.CopyDao
import com.goldenoaks.library.data.model.Copy
import com.goldenoaks.library.data.model.CopyStatus
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CopyRepository @Inject constructor(
    private val copyDao: CopyDao
) {
    suspend fun getCopyById(copyId: Long): Copy? = copyDao.getCopyById(copyId)

    suspend fun getCopyByBarcode(barcode: String): Copy? = copyDao.getCopyByBarcode(barcode)

    fun getCopiesByBookId(bookId: Long): Flow<List<Copy>> = copyDao.getCopiesByBookId(bookId)

    fun getCopiesByBookIdAndStatus(bookId: Long, status: CopyStatus): Flow<List<Copy>> =
        copyDao.getCopiesByBookIdAndStatus(bookId, status)

    fun getCopiesByStatus(status: CopyStatus): Flow<List<Copy>> = copyDao.getCopiesByStatus(status)

    suspend fun insertCopy(copy: Copy): Long = copyDao.insertCopy(copy)

    suspend fun updateCopyStatus(copyId: Long, status: CopyStatus, location: String?) =
        copyDao.updateCopyStatus(copyId, status, location)

    suspend fun deleteCopy(copyId: Long) = copyDao.deleteCopy(copyId)
}

