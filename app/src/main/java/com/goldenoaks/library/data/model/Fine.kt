package com.goldenoaks.library.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "fines",
    foreignKeys = [
        ForeignKey(
            entity = Loan::class,
            parentColumns = ["loanId"],
            childColumns = ["loanId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["loanId"]), Index(value = ["status"])]
)
data class Fine(
    @PrimaryKey(autoGenerate = true)
    val fineId: Long = 0,
    val loanId: Long,
    val amount: Double,
    val status: FineStatus,
    val assessedDate: Long,
    val paidDate: Long? = null
)

enum class FineStatus {
    PENDING,
    PAID,
    WAIVED
}

