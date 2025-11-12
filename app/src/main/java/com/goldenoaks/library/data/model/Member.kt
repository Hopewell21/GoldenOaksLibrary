package com.goldenoaks.library.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "members")
data class Member(
    @PrimaryKey(autoGenerate = true)
    val memberId: Long = 0,
    val name: String,
    val contactNumber: String? = null,
    val address: String? = null,
    val email: String? = null,
    val status: MemberStatus,
    val joinDate: Long = System.currentTimeMillis()
)

enum class MemberStatus {
    ACTIVE,
    INACTIVE,
    SUSPENDED
}

