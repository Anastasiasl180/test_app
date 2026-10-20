package com.example.testapp.ui.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.ui.game.logic.LevelConfig
import com.example.testapp.notifications.DAILY_REWARD_COINS
import com.example.testapp.notifications.DailyRewardScheduler
import com.example.testapp.notifications.dailyRewardTimeLeft
import com.example.testapp.storage.AppStorage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel


@KoinViewModel
class MenuViewModel(
    private val storage: AppStorage,
    private val rewardScheduler: DailyRewardScheduler,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MenuUiState())
    val uiState: StateFlow<MenuUiState> = _uiState.asStateFlow()

    private val _navEvents = MutableSharedFlow<MenuNavEvent>()
    val navEvents = _navEvents.asSharedFlow()

    init {
        refreshBalance()
    }

    fun refreshBalance() {
        _uiState.value = _uiState.value.copy(
            balance = storage.loadPlayer().balance,
            dailyRewardAvailable = dailyRewardTimeLeft() <= 0
        )
    }

    fun claimDailyReward(): Boolean {
        if (dailyRewardTimeLeft() > 0) {
            refreshBalance()
            return false
        }
        val player = storage.loadPlayer()
        val updated = player.copy(
            balance = player.balance + DAILY_REWARD_COINS,
            lastDailyReward = System.currentTimeMillis()
        )
        storage.persistPlayer(updated)
        rewardScheduler.scheduleReminder()
        _uiState.value = _uiState.value.copy(balance = updated.balance, dailyRewardAvailable = false)
        return true
    }

    fun dailyRewardTimeLeft(): Long = dailyRewardTimeLeft(storage.loadPlayer().lastDailyReward)

    fun onAction(action: MenuAction) {
        if (action == MenuAction.NavigateToPlay) selectNextLevel()
        viewModelScope.launch {
            _navEvents.emit(
                when (action) {
                    MenuAction.NavigateToPlay -> MenuNavEvent.NavigateToGame
                    MenuAction.NavigateToLevels -> MenuNavEvent.NavigateToLevels
                    MenuAction.NavigateToShop -> MenuNavEvent.NavigateToShop
                    MenuAction.NavigateToProfile -> MenuNavEvent.NavigateToProfile
                    MenuAction.NavigateToSettings -> MenuNavEvent.NavigateToSettings
                }
            )
        }
    }

    private fun selectNextLevel() {
        val progress = storage.loadProgress()
        val next = ((progress.completedLevels.maxOrNull() ?: 0) + 1)
            .coerceAtMost(LevelConfig.LEVELS_COUNT)
        storage.persistProgress(progress.copy(selectedLevel = next))
    }
}

data class MenuUiState(
    val balance: Int = 0,
    val dailyRewardAvailable: Boolean = false,
)

sealed interface MenuAction {
    data object NavigateToPlay : MenuAction
    data object NavigateToLevels : MenuAction
    data object NavigateToShop : MenuAction
    data object NavigateToProfile : MenuAction
    data object NavigateToSettings : MenuAction
}

sealed interface MenuNavEvent {
    data object NavigateToGame : MenuNavEvent
    data object NavigateToLevels : MenuNavEvent
    data object NavigateToShop : MenuNavEvent
    data object NavigateToProfile : MenuNavEvent
    data object NavigateToSettings : MenuNavEvent
}
