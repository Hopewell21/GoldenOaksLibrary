package com.goldenoaks.library.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Long = 0,
    val email: String,
    val name: String,
    val passwordHash: String,
    val role: UserRole,
    val createdAt: Long = System.currentTimeMillis()
)

enum class UserRole {
    PATRON,
    LIBRARIAN,
    ADMIN
}

