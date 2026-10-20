package com.example.testapp.ui.levels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.ui.game.logic.LevelConfig
import com.example.testapp.storage.AppStorage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel


@KoinViewModel
class LevelsViewModel(private val storage: AppStorage) : ViewModel() {

    private val _passedLevels = mutableStateOf(storage.loadProgress().completedLevels)
    val passedLevels: State<Set<Int>> = _passedLevels

    private val _levelStars = mutableStateOf(storage.loadProgress().levelStars)
    val levelStars: State<Map<Int, Int>> = _levelStars

    val allLevels: List<Int> = (1..LevelConfig.LEVELS_COUNT).toList()

    private val _event = MutableSharedFlow<LevelsUiEvent>()
    val event = _event.asSharedFlow()

    fun onEvent(event: LevelsEvent) {
        when (event) {
            LevelsEvent.NavigateBack -> {
                viewModelScope.launch {
                    _event.emit(LevelsUiEvent.NavigateBack)
                }
            }

            LevelsEvent.NavigateToRules -> {
                viewModelScope.launch {
                    _event.emit(LevelsUiEvent.NavigateToRules)
                }
            }

            is LevelsEvent.NavigateToGame -> {
                viewModelScope.launch {
                    storage.persistProgress(
                        storage.loadProgress().copy(selectedLevel = event.level)
                    )
                    _event.emit(LevelsUiEvent.NavigateToGame)
                }
            }
        }
    }

}

sealed interface LevelsEvent {
    data class NavigateToGame(val level: Int) : LevelsEvent
    data object NavigateBack : LevelsEvent
    data object NavigateToRules : LevelsEvent
}

sealed class LevelsUiEvent {
    data object NavigateBack : LevelsUiEvent()
    data object NavigateToGame : LevelsUiEvent()
    data object NavigateToRules : LevelsUiEvent()
}
