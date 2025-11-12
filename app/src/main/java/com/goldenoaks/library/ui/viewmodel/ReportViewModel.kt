package com.goldenoaks.library.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goldenoaks.library.data.model.Book
import com.goldenoaks.library.data.model.CopyStatus
import com.goldenoaks.library.data.model.Loan
import com.goldenoaks.library.data.repository.BookRepository
import com.goldenoaks.library.data.repository.CopyRepository
import com.goldenoaks.library.data.repository.LoanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ActivityReport(
    val totalLoans: Int,
    val activeLoans: Int,
    val returnedLoans: Int,
    val overdueLoans: Int,
    val loansByDateRange: Map<String, Int>
)

data class InventoryReport(
    val totalBooks: Int,
    val totalCopies: Int,
    val availableCopies: Int,
    val onLoanCopies: Int,
    val booksByGenre: Map<String, Int>
)

data class ReportState(
    val activityReport: ActivityReport? = null,
    val inventoryReport: InventoryReport? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val loanRepository: LoanRepository,
    private val bookRepository: BookRepository,
    private val copyRepository: CopyRepository
) : ViewModel() {

    private val _reportState = MutableStateFlow(ReportState())
    val reportState: StateFlow<ReportState> = _reportState.asStateFlow()

    fun generateActivityReport(startDate: Long? = null, endDate: Long? = null) {
        viewModelScope.launch {
            _reportState.value = _reportState.value.copy(isLoading = true)
            try {
                loanRepository.getAllLoans().collect { allLoans ->
                    val filteredLoans = if (startDate != null && endDate != null) {
                        allLoans.filter { it.issueDate in startDate..endDate }
                    } else {
                        allLoans
                    }

                    val totalLoans = filteredLoans.size
                    val activeLoans = filteredLoans.count { it.returnDate == null }
                    val returnedLoans = filteredLoans.count { it.returnDate != null }
                    val overdueLoans = filteredLoans.count {
                        it.returnDate == null && it.dueDate < System.currentTimeMillis()
                    }

                    val loansByDateRange = filteredLoans.groupBy {
                        java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                            .format(java.util.Date(it.issueDate))
                    }.mapValues { it.value.size }

                    val report = ActivityReport(
                        totalLoans = totalLoans,
                        activeLoans = activeLoans,
                        returnedLoans = returnedLoans,
                        overdueLoans = overdueLoans,
                        loansByDateRange = loansByDateRange
                    )

                    _reportState.value = _reportState.value.copy(
                        activityReport = report,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _reportState.value = _reportState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to generate activity report: ${e.message}"
                )
            }
        }
    }

    fun generateInventoryReport() {
        viewModelScope.launch {
            _reportState.value = _reportState.value.copy(isLoading = true)
            try {
                bookRepository.getAllBooks().collect { books ->
                    var totalCopies = 0
                    var availableCopies = 0
                    var onLoanCopies = 0
                    val booksByGenre = mutableMapOf<String, Int>()

                    for (book in books) {
                        booksByGenre[book.genre] = (booksByGenre[book.genre] ?: 0) + 1

                        val copies = copyRepository.getCopiesByBookId(book.bookId).first()
                        totalCopies += copies.size
                        availableCopies += copies.count { it.status == CopyStatus.AVAILABLE }
                        onLoanCopies += copies.count { it.status == CopyStatus.ON_LOAN }
                    }

                    val report = InventoryReport(
                        totalBooks = books.size,
                        totalCopies = totalCopies,
                        availableCopies = availableCopies,
                        onLoanCopies = onLoanCopies,
                        booksByGenre = booksByGenre
                    )

                    _reportState.value = _reportState.value.copy(
                        inventoryReport = report,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _reportState.value = _reportState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to generate inventory report: ${e.message}"
                )
            }
        }
    }

    fun clearError() {
        _reportState.value = _reportState.value.copy(errorMessage = null)
    }
}

