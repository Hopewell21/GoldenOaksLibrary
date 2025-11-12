package com.goldenoaks.library.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goldenoaks.library.data.model.Fine
import com.goldenoaks.library.data.model.FineStatus
import com.goldenoaks.library.data.model.Loan
import com.goldenoaks.library.data.repository.FineRepository
import com.goldenoaks.library.data.repository.LoanRepository
import com.goldenoaks.library.util.FineCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FineState(
    val fines: List<Fine> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class FineViewModel @Inject constructor(
    private val fineRepository: FineRepository,
    private val loanRepository: LoanRepository
) : ViewModel() {

    private val _fineState = MutableStateFlow(FineState())
    val fineState: StateFlow<FineState> = _fineState.asStateFlow()

    fun loadFinesByMember(memberId: Long) {
        viewModelScope.launch {
            _fineState.value = _fineState.value.copy(isLoading = true)
            try {
                fineRepository.getFinesByMemberId(memberId).collect { fines ->
                    _fineState.value = _fineState.value.copy(
                        fines = fines,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _fineState.value = _fineState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to load fines: ${e.message}"
                )
            }
        }
    }

    fun loadPendingFines() {
        viewModelScope.launch {
            _fineState.value = _fineState.value.copy(isLoading = true)
            try {
                fineRepository.getFinesByStatus(FineStatus.PENDING).collect { fines ->
                    _fineState.value = _fineState.value.copy(
                        fines = fines,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _fineState.value = _fineState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to load fines: ${e.message}"
                )
            }
        }
    }

    fun calculateAndAssessFines() {
        viewModelScope.launch {
            try {
                loanRepository.getOverdueLoans().collect { overdueLoans ->
                    for (loan in overdueLoans) {
                        val existingFines = fineRepository.getFinesByLoanId(loan.loanId).first()
                        val hasPendingFine = existingFines.any { it.status == FineStatus.PENDING }

                        if (!hasPendingFine) {
                            val fineAmount = FineCalculator.calculateFine(loan.dueDate)
                            if (fineAmount > 0) {
                                val fine = Fine(
                                    loanId = loan.loanId,
                                    amount = fineAmount,
                                    status = FineStatus.PENDING,
                                    assessedDate = System.currentTimeMillis()
                                )
                                fineRepository.insertFine(fine)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                _fineState.value = _fineState.value.copy(
                    errorMessage = "Failed to assess fines: ${e.message}"
                )
            }
        }
    }

    fun payFine(fineId: Long) {
        viewModelScope.launch {
            _fineState.value = _fineState.value.copy(isLoading = true)
            try {
                fineRepository.updateFineStatus(fineId, FineStatus.PAID, System.currentTimeMillis())
                loadPendingFines()
            } catch (e: Exception) {
                _fineState.value = _fineState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to pay fine: ${e.message}"
                )
            }
        }
    }

    fun waiveFine(fineId: Long) {
        viewModelScope.launch {
            _fineState.value = _fineState.value.copy(isLoading = true)
            try {
                fineRepository.updateFineStatus(fineId, FineStatus.WAIVED, null)
                loadPendingFines()
            } catch (e: Exception) {
                _fineState.value = _fineState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to waive fine: ${e.message}"
                )
            }
        }
    }

    fun clearError() {
        _fineState.value = _fineState.value.copy(errorMessage = null)
    }
}

