package com.goldenoaks.library.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "copies",
    foreignKeys = [
        ForeignKey(
            entity = Book::class,
            parentColumns = ["bookId"],
            childColumns = ["bookId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["barcode"], unique = true), Index(value = ["bookId"])]
)
data class Copy(
    @PrimaryKey(autoGenerate = true)
    val copyId: Long = 0,
    val bookId: Long,
    val barcode: String,
    val status: CopyStatus,
    val location: String? = null
)

enum class CopyStatus {
    AVAILABLE,
    ON_LOAN,
    RESERVED,
    DAMAGED,
    LOST
}

