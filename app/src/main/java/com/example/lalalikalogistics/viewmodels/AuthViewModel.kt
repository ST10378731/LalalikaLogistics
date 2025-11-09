package com.example.lalalikalogistics.viewmodels

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val userId: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(private val context: android.content.Context?) {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun signIn(email: String, password: String, onSuccess: () -> Unit) {
        _authState.value = AuthState.Loading
        // Implement Firebase sign-in here if needed, or use the direct call in AuthScreen
        onSuccess()
        _authState.value = AuthState.Success("userId")
    }

    fun register(email: String, password: String, onSuccess: () -> Unit) {
        _authState.value = AuthState.Loading
        // Implement Firebase register here if needed
        onSuccess()
        _authState.value = AuthState.Success("userId")
    }
}