package com.goldenoaks.library.data.repository

import com.goldenoaks.library.data.dao.MemberDao
import com.goldenoaks.library.data.model.Member
import com.goldenoaks.library.data.model.MemberStatus
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemberRepository @Inject constructor(
    private val memberDao: MemberDao
) {
    suspend fun getMemberById(memberId: Long): Member? = memberDao.getMemberById(memberId)

    suspend fun getMemberByEmail(email: String): Member? = memberDao.getMemberByEmail(email)

    fun getMembersByStatus(status: MemberStatus): Flow<List<Member>> =
        memberDao.getMembersByStatus(status)

    fun searchMembers(query: String): Flow<List<Member>> = memberDao.searchMembers(query)

    fun getAllMembers(): Flow<List<Member>> = memberDao.getAllMembers()

    suspend fun insertMember(member: Member): Long = memberDao.insertMember(member)

    suspend fun updateMember(
        memberId: Long,
        name: String,
        contactNumber: String?,
        address: String?,
        email: String?,
        status: MemberStatus
    ) = memberDao.updateMember(memberId, name, contactNumber, address, email, status)

    suspend fun deleteMember(memberId: Long) = memberDao.deleteMember(memberId)
}

