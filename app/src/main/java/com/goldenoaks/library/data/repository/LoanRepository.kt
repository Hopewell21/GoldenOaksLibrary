package com.goldenoaks.library.data.repository

import com.goldenoaks.library.data.dao.LoanDao
import com.goldenoaks.library.data.model.Loan
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoanRepository @Inject constructor(
    private val loanDao: LoanDao
) {
    suspend fun getLoanById(loanId: Long): Loan? = loanDao.getLoanById(loanId)

    fun getActiveLoansByMember(memberId: Long): Flow<List<Loan>> =
        loanDao.getActiveLoansByMember(memberId)

    fun getAllLoansByMember(memberId: Long): Flow<List<Loan>> = loanDao.getAllLoansByMember(memberId)

    suspend fun getActiveLoanByCopy(copyId: Long): Loan? = loanDao.getActiveLoanByCopy(copyId)

    fun getOverdueLoans(currentTime: Long = System.currentTimeMillis()): Flow<List<Loan>> =
        loanDao.getOverdueLoans(currentTime)

    fun getAllActiveLoans(): Flow<List<Loan>> = loanDao.getAllActiveLoans()

    fun getAllLoans(): Flow<List<Loan>> = loanDao.getAllLoans()

    suspend fun insertLoan(loan: Loan): Long = loanDao.insertLoan(loan)

    suspend fun returnLoan(loanId: Long, returnDate: Long) = loanDao.returnLoan(loanId, returnDate)

    suspend fun deleteLoan(loanId: Long) = loanDao.deleteLoan(loanId)
}

