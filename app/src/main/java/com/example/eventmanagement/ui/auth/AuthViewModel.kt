package com.example.eventmanagement.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventmanagement.data.repository.AuthRepository
import com.example.eventmanagement.utils.UiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AuthViewModel(private val repo: AuthRepository = AuthRepository()) : ViewModel() {
    private val _state = MutableStateFlow<UiState<Unit>>(UiState.Idle);
    val state = _state.asStateFlow();
    fun login(e: String, p: String) = run(e, p) { repo.login(e, p) };
    fun signup(e: String, p: String) = run(e, p) { repo.signup(e, p) };
    fun reset(e: String) = viewModelScope.launch {
        if (e.isBlank()) {
            _state.value = UiState.Error("Email is required"); return@launch
        }; _state.value = UiState.Loading; runCatching { repo.reset(e) }.onSuccess {
        _state.value = UiState.Success(Unit)
    }.onFailure { _state.value = UiState.Error(it.message ?: "Reset failed") }
    };

    private fun run(e: String, p: String, block: suspend () -> Any) {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(e).matches()) {
            _state.value = UiState.Error("Enter a valid email"); return
        }; if (p.length < 6) {
            _state.value = UiState.Error("Password must be at least 6 characters"); return
        }; viewModelScope.launch {
            _state.value = UiState.Loading; runCatching { block() }.onSuccess {
            _state.value = UiState.Success(Unit)
        }.onFailure { _state.value = UiState.Error(it.message ?: "Authentication failed") }
        }
    }
}
