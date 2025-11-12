package com.goldenoaks.library.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goldenoaks.library.data.model.Member
import com.goldenoaks.library.data.model.MemberStatus
import com.goldenoaks.library.data.repository.MemberRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MemberState(
    val members: List<Member> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class MemberViewModel @Inject constructor(
    private val memberRepository: MemberRepository
) : ViewModel() {

    private val _memberState = MutableStateFlow(MemberState())
    val memberState: StateFlow<MemberState> = _memberState.asStateFlow()

    init {
        loadAllMembers()
    }

    private fun loadAllMembers() {
        viewModelScope.launch {
            _memberState.value = _memberState.value.copy(isLoading = true)
            try {
                memberRepository.getAllMembers().collect { members ->
                    _memberState.value = _memberState.value.copy(
                        members = members,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _memberState.value = _memberState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to load members: ${e.message}"
                )
            }
        }
    }

    fun searchMembers(query: String) {
        _memberState.value = _memberState.value.copy(searchQuery = query)
        viewModelScope.launch {
            _memberState.value = _memberState.value.copy(isLoading = true)
            try {
                if (query.isBlank()) {
                    loadAllMembers()
                } else {
                    memberRepository.searchMembers(query).collect { members ->
                        _memberState.value = _memberState.value.copy(
                            members = members,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _memberState.value = _memberState.value.copy(
                    isLoading = false,
                    errorMessage = "Search failed: ${e.message}"
                )
            }
        }
    }

    fun addMember(
        name: String,
        contactNumber: String?,
        address: String?,
        email: String?
    ) {
        viewModelScope.launch {
            try {
                val member = Member(
                    name = name,
                    contactNumber = contactNumber,
                    address = address,
                    email = email,
                    status = MemberStatus.ACTIVE
                )
                memberRepository.insertMember(member)
                loadAllMembers()
            } catch (e: Exception) {
                _memberState.value = _memberState.value.copy(
                    errorMessage = "Failed to add member: ${e.message}"
                )
            }
        }
    }

    fun updateMember(
        memberId: Long,
        name: String,
        contactNumber: String?,
        address: String?,
        email: String?,
        status: MemberStatus
    ) {
        viewModelScope.launch {
            try {
                memberRepository.updateMember(memberId, name, contactNumber, address, email, status)
                loadAllMembers()
            } catch (e: Exception) {
                _memberState.value = _memberState.value.copy(
                    errorMessage = "Failed to update member: ${e.message}"
                )
            }
        }
    }

    fun deleteMember(memberId: Long) {
        viewModelScope.launch {
            try {
                memberRepository.deleteMember(memberId)
                loadAllMembers()
            } catch (e: Exception) {
                _memberState.value = _memberState.value.copy(
                    errorMessage = "Failed to delete member: ${e.message}"
                )
            }
        }
    }

    fun clearError() {
        _memberState.value = _memberState.value.copy(errorMessage = null)
    }
}

