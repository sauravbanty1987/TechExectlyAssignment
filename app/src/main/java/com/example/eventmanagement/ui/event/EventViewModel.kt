package com.example.eventmanagement.ui.event

import androidx.lifecycle.*
import com.example.eventmanagement.data.model.Event
import com.example.eventmanagement.data.repository.EventRepository
import com.example.eventmanagement.utils.UiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class EventViewModel(private val repo: EventRepository = EventRepository()) : ViewModel() {
    private val _events = MutableStateFlow<List<Event>>(emptyList());
    val events = _events.asStateFlow();
    private val _state = MutableStateFlow<UiState<Unit>>(UiState.Idle);
    val state = _state.asStateFlow();

    init {
        viewModelScope.launch {
            runCatching {
                repo.observe().collect { _events.value = it }
            }.onFailure { _state.value = UiState.Error(it.message ?: "Load failed") }
        }
    };
    fun add(e: Event) = op { repo.add(e) };
    fun update(e: Event) = op { repo.update(e) };
    fun delete(id: String) = op { repo.delete(id) };
    private fun op(b: suspend () -> Unit) = viewModelScope.launch {
        _state.value = UiState.Loading; runCatching { b() }.onSuccess {
        _state.value = UiState.Success(Unit)
    }.onFailure { _state.value = UiState.Error(it.message ?: "Operation failed") }
    }
}
