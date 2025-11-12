package com.goldenoaks.library.data.database

import androidx.room.TypeConverter
import com.goldenoaks.library.data.model.CopyStatus
import com.goldenoaks.library.data.model.FineStatus
import com.goldenoaks.library.data.model.MemberStatus
import com.goldenoaks.library.data.model.UserRole

class Converters {
    @TypeConverter
    fun fromUserRole(value: UserRole): String = value.name

    @TypeConverter
    fun toUserRole(value: String): UserRole = UserRole.valueOf(value)

    @TypeConverter
    fun fromMemberStatus(value: MemberStatus): String = value.name

    @TypeConverter
    fun toMemberStatus(value: String): MemberStatus = MemberStatus.valueOf(value)

    @TypeConverter
    fun fromCopyStatus(value: CopyStatus): String = value.name

    @TypeConverter
    fun toCopyStatus(value: String): CopyStatus = CopyStatus.valueOf(value)

    @TypeConverter
    fun fromFineStatus(value: FineStatus): String = value.name

    @TypeConverter
    fun toFineStatus(value: String): FineStatus = FineStatus.valueOf(value)
}

