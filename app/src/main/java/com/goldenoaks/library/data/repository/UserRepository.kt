package com.goldenoaks.library.data.repository

import com.goldenoaks.library.data.dao.UserDao
import com.goldenoaks.library.data.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao
) {
    suspend fun getUserByEmail(email: String): User? = userDao.getUserByEmail(email)

    suspend fun getUserById(userId: Long): User? = userDao.getUserById(userId)

    fun getAllUsers(): Flow<List<User>> = userDao.getAllUsers()

    suspend fun insertUser(user: User): Long = userDao.insertUser(user)

    suspend fun updateUser(userId: Long, name: String, email: String) =
        userDao.updateUser(userId, name, email)

    suspend fun deleteUser(userId: Long) = userDao.deleteUser(userId)
}

