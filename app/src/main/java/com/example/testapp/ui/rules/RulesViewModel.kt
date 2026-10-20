package com.example.testapp.ui.rules

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.storage.AppStorage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel


@KoinViewModel
class RulesViewModel(storage: AppStorage) : ViewModel() {

    private val _balance = mutableStateOf(storage.loadPlayer().balance)
    val balance: State<Int> = _balance

    private val _event = MutableSharedFlow<RulesUiEvent>()
    val event = _event.asSharedFlow()

    fun onEvent(event: RulesEvent) {
        when (event) {
            RulesEvent.NavigateBack -> {
                viewModelScope.launch {
                    _event.emit(RulesUiEvent.NavigateBack)
                }
            }
        }
    }
}

sealed interface RulesEvent {
    data object NavigateBack : RulesEvent
}

sealed class RulesUiEvent {
    data object NavigateBack : RulesUiEvent()
}
