package com.example.testapp.ui.profile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.ui.game.logic.Achievement
import com.example.testapp.ui.game.logic.LevelConfig
import com.example.testapp.storage.AppStorage
import com.example.testapp.storage.Player
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import java.io.File


@KoinViewModel
class ProfileViewModel(private val storage: AppStorage) : ViewModel() {

    private val _player = mutableStateOf(storage.loadPlayer())
    val player: State<Player> = _player

    private val progress = storage.loadProgress()

    val levelsPlayed: Int = progress.completedLevels.size
    val bestScore: Int = progress.bestScores.values.maxOrNull() ?: 0
    val currentLevel: Int = ((progress.completedLevels.maxOrNull() ?: 0) + 1)
        .coerceAtMost(LevelConfig.LEVELS_COUNT)
    val unlockedAchievements: Set<Achievement> = progress.achievements
    val allAchievements: List<Achievement> = Achievement.entries

    private val _event = MutableSharedFlow<ProfileUiEvent>()
    val event = _event.asSharedFlow()

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.ChangeName -> changeName(event.name)
            is ProfileEvent.ChangeAvatar -> changeAvatar(event.path)
            ProfileEvent.NavigateBack -> emit(ProfileUiEvent.NavigateBack)
        }
    }

    private fun changeName(name: String) {
        val updated = _player.value.copy(nickname = name.uppercase().take(NAME_MAX_LENGTH))
        storage.persistPlayer(updated)
        _player.value = updated
    }

    private fun changeAvatar(path: String) {
        val oldPhoto = _player.value.photoUri
        val updated = _player.value.copy(photoUri = path)
        storage.persistPlayer(updated)
        _player.value = updated
        if (oldPhoto != null) File(oldPhoto.toUri().path.orEmpty()).delete()
    }

    private fun emit(event: ProfileUiEvent) {
        viewModelScope.launch { _event.emit(event) }
    }

    companion object {
        private const val NAME_MAX_LENGTH = 14
    }
}

sealed interface ProfileEvent {
    data class ChangeName(val name: String) : ProfileEvent
    data class ChangeAvatar(val path: String) : ProfileEvent
    data object NavigateBack : ProfileEvent
}

sealed class ProfileUiEvent {
    data object NavigateBack : ProfileUiEvent()
}
