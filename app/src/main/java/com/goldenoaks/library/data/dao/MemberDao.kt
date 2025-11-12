package com.goldenoaks.library.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.goldenoaks.library.data.model.Member
import com.goldenoaks.library.data.model.MemberStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {
    @Query("SELECT * FROM members WHERE memberId = :memberId LIMIT 1")
    suspend fun getMemberById(memberId: Long): Member?

    @Query("SELECT * FROM members WHERE email = :email LIMIT 1")
    suspend fun getMemberByEmail(email: String): Member?

    @Query("SELECT * FROM members WHERE status = :status")
    fun getMembersByStatus(status: MemberStatus): Flow<List<Member>>

    @Query("SELECT * FROM members WHERE name LIKE '%' || :query || '%' OR email LIKE '%' || :query || '%'")
    fun searchMembers(query: String): Flow<List<Member>>

    @Query("SELECT * FROM members")
    fun getAllMembers(): Flow<List<Member>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: Member): Long

    @Query("UPDATE members SET name = :name, contactNumber = :contactNumber, address = :address, email = :email, status = :status WHERE memberId = :memberId")
    suspend fun updateMember(memberId: Long, name: String, contactNumber: String?, address: String?, email: String?, status: MemberStatus)

    @Query("DELETE FROM members WHERE memberId = :memberId")
    suspend fun deleteMember(memberId: Long)
}

