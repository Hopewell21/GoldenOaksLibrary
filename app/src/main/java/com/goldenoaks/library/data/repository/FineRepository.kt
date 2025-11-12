package com.goldenoaks.library.data.repository

import com.goldenoaks.library.data.dao.FineDao
import com.goldenoaks.library.data.model.Fine
import com.goldenoaks.library.data.model.FineStatus
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FineRepository @Inject constructor(
    private val fineDao: FineDao
) {
    suspend fun getFineById(fineId: Long): Fine? = fineDao.getFineById(fineId)

    fun getFinesByLoanId(loanId: Long): Flow<List<Fine>> = fineDao.getFinesByLoanId(loanId)

    fun getFinesByMemberId(memberId: Long): Flow<List<Fine>> = fineDao.getFinesByMemberId(memberId)

    fun getFinesByMemberIdAndStatus(memberId: Long, status: FineStatus): Flow<List<Fine>> =
        fineDao.getFinesByMemberIdAndStatus(memberId, status)

    fun getFinesByStatus(status: FineStatus): Flow<List<Fine>> = fineDao.getFinesByStatus(status)

    fun getAllFines(): Flow<List<Fine>> = fineDao.getAllFines()

    suspend fun insertFine(fine: Fine): Long = fineDao.insertFine(fine)

    suspend fun updateFineStatus(fineId: Long, status: FineStatus, paidDate: Long?) =
        fineDao.updateFineStatus(fineId, status, paidDate)

    suspend fun deleteFine(fineId: Long) = fineDao.deleteFine(fineId)
}

