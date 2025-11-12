package com.goldenoaks.library.util

object FineCalculator {
    // Default fine rate: R5 per day overdue
    private const val DEFAULT_FINE_RATE_PER_DAY = 5.0

    fun calculateFine(dueDate: Long, returnDate: Long? = null, fineRatePerDay: Double = DEFAULT_FINE_RATE_PER_DAY): Double {
        val currentTime = returnDate ?: System.currentTimeMillis()
        if (currentTime <= dueDate) return 0.0

        val daysOverdue = ((currentTime - dueDate) / (1000 * 60 * 60 * 24)).toInt()
        return daysOverdue * fineRatePerDay
    }
}

