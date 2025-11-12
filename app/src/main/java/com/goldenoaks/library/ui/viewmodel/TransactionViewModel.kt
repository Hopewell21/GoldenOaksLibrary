package com.goldenoaks.library.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goldenoaks.library.data.model.CopyStatus
import com.goldenoaks.library.data.model.Loan
import com.goldenoaks.library.data.repository.CopyRepository
import com.goldenoaks.library.data.repository.LoanRepository
import com.goldenoaks.library.data.repository.MemberRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TransactionState(
    val activeLoans: List<Loan> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val loanRepository: LoanRepository,
    private val copyRepository: CopyRepository,
    private val memberRepository: MemberRepository
) : ViewModel() {

    private val _transactionState = MutableStateFlow(TransactionState())
    val transactionState: StateFlow<TransactionState> = _transactionState.asStateFlow()

    init {
        loadActiveLoans()
    }

    private fun loadActiveLoans() {
        viewModelScope.launch {
            _transactionState.value = _transactionState.value.copy(isLoading = true)
            try {
                loanRepository.getAllActiveLoans().collect { loans ->
                    _transactionState.value = _transactionState.value.copy(
                        activeLoans = loans,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _transactionState.value = _transactionState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to load loans: ${e.message}"
                )
            }
        }
    }

    fun issueBook(memberId: Long, copyId: Long, loanDays: Int = 14) {
        viewModelScope.launch {
            _transactionState.value = _transactionState.value.copy(isLoading = true)
            try {
                val member = memberRepository.getMemberById(memberId)
                if (member == null) {
                    _transactionState.value = _transactionState.value.copy(
                        isLoading = false,
                        errorMessage = "Member not found"
                    )
                    return@launch
                }

                val copy = copyRepository.getCopyById(copyId)
                if (copy == null) {
                    _transactionState.value = _transactionState.value.copy(
                        isLoading = false,
                        errorMessage = "Copy not found"
                    )
                    return@launch
                }

                if (copy.status != CopyStatus.AVAILABLE) {
                    _transactionState.value = _transactionState.value.copy(
                        isLoading = false,
                        errorMessage = "Copy is not available"
                    )
                    return@launch
                }

                val issueDate = System.currentTimeMillis()
                val dueDate = issueDate + (loanDays * 24 * 60 * 60 * 1000L)

                val loan = Loan(
                    memberId = memberId,
                    copyId = copyId,
                    issueDate = issueDate,
                    dueDate = dueDate
                )

                loanRepository.insertLoan(loan)
                copyRepository.updateCopyStatus(copyId, CopyStatus.ON_LOAN, null)
                loadActiveLoans()
            } catch (e: Exception) {
                _transactionState.value = _transactionState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to issue book: ${e.message}"
                )
            }
        }
    }

    fun returnBook(loanId: Long) {
        viewModelScope.launch {
            _transactionState.value = _transactionState.value.copy(isLoading = true)
            try {
                val loan = loanRepository.getLoanById(loanId)
                if (loan == null) {
                    _transactionState.value = _transactionState.value.copy(
                        isLoading = false,
                        errorMessage = "Loan not found"
                    )
                    return@launch
                }

                val returnDate = System.currentTimeMillis()
                loanRepository.returnLoan(loanId, returnDate)
                copyRepository.updateCopyStatus(loan.copyId, CopyStatus.AVAILABLE, null)
                loadActiveLoans()
            } catch (e: Exception) {
                _transactionState.value = _transactionState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to return book: ${e.message}"
                )
            }
        }
    }

    fun getActiveLoansByMember(memberId: Long) {
        viewModelScope.launch {
            _transactionState.value = _transactionState.value.copy(isLoading = true)
            try {
                loanRepository.getActiveLoansByMember(memberId).collect { loans ->
                    _transactionState.value = _transactionState.value.copy(
                        activeLoans = loans,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _transactionState.value = _transactionState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to load loans: ${e.message}"
                )
            }
        }
    }

    fun clearError() {
        _transactionState.value = _transactionState.value.copy(errorMessage = null)
    }
}

