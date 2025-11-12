package com.goldenoaks.library.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "books",
    indices = [Index(value = ["isbn"], unique = true), Index(value = ["title"])]
)
data class Book(
    @PrimaryKey(autoGenerate = true)
    val bookId: Long = 0,
    val isbn: String,
    val title: String,
    val author: String,
    val genre: String,
    val publisher: String? = null,
    val year: Int? = null,
    val description: String? = null
)

