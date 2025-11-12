package com.goldenoaks.library.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "loans",
    foreignKeys = [
        ForeignKey(
            entity = Member::class,
            parentColumns = ["memberId"],
            childColumns = ["memberId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Copy::class,
            parentColumns = ["copyId"],
            childColumns = ["copyId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["memberId"]), Index(value = ["copyId"]), Index(value = ["dueDate"])]
)
data class Loan(
    @PrimaryKey(autoGenerate = true)
    val loanId: Long = 0,
    val memberId: Long,
    val copyId: Long,
    val issueDate: Long,
    val dueDate: Long,
    val returnDate: Long? = null
)

