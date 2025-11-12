package com.goldenoaks.library.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goldenoaks.library.data.model.User
import com.goldenoaks.library.data.model.UserRole
import com.goldenoaks.library.data.repository.BookRepository
import com.goldenoaks.library.data.repository.CopyRepository
import com.goldenoaks.library.data.repository.MemberRepository
import com.goldenoaks.library.data.repository.UserRepository
import com.goldenoaks.library.util.DatabaseInitializer
import com.goldenoaks.library.util.PasswordHasher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthState(
    val isAuthenticated: Boolean = false,
    val currentUser: User? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val bookRepository: BookRepository,
    private val copyRepository: CopyRepository,
    private val memberRepository: MemberRepository
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        // Check if user is already logged in (in a real app, use SharedPreferences or secure storage)
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, errorMessage = null)
            try {
                val user = userRepository.getUserByEmail(email)
                if (user != null && PasswordHasher.verifyPassword(password, user.passwordHash)) {
                    _authState.value = _authState.value.copy(
                        isAuthenticated = true,
                        currentUser = user,
                        isLoading = false
                    )
                } else {
                    _authState.value = _authState.value.copy(
                        isLoading = false,
                        errorMessage = "Invalid email or password"
                    )
                }
            } catch (e: Exception) {
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    errorMessage = "Login failed: ${e.message}"
                )
            }
        }
    }

    fun logout() {
        _authState.value = AuthState()
    }

    fun register(name: String, email: String, password: String, role: UserRole = UserRole.PATRON) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, errorMessage = null)
            try {
                val existingUser = userRepository.getUserByEmail(email)
                if (existingUser != null) {
                    _authState.value = _authState.value.copy(
                        isLoading = false,
                        errorMessage = "Email already registered"
                    )
                    return@launch
                }

                val passwordHash = PasswordHasher.hashPassword(password)
                val newUser = User(
                    email = email,
                    name = name,
                    passwordHash = passwordHash,
                    role = role
                )
                val userId = userRepository.insertUser(newUser)
                val createdUser = userRepository.getUserById(userId)

                _authState.value = _authState.value.copy(
                    isAuthenticated = true,
                    currentUser = createdUser,
                    isLoading = false
                )
            } catch (e: Exception) {
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    errorMessage = "Registration failed: ${e.message}"
                )
            }
        }
    }

    fun clearError() {
        _authState.value = _authState.value.copy(errorMessage = null)
    }

    fun initializeDatabase() {
        viewModelScope.launch {
            DatabaseInitializer.initializeDatabase(
                userRepository,
                bookRepository,
                copyRepository,
                memberRepository
            )
        }
    }
}

