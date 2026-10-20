package com.example.testapp.ui.shop

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.ui.game.logic.Skin
import com.example.testapp.storage.AppStorage
import com.example.testapp.storage.Player
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel


@KoinViewModel
class ShopViewModel(private val storage: AppStorage) : ViewModel() {

    private val _player = mutableStateOf(storage.loadPlayer())
    val player: State<Player> = _player

    val skins: List<Skin> = Skin.entries

    private val _event = MutableSharedFlow<ShopUiEvent>()
    val event = _event.asSharedFlow()

    fun onEvent(event: ShopEvent) {
        when (event) {
            ShopEvent.NavigateBack -> {
                viewModelScope.launch {
                    _event.emit(ShopUiEvent.NavigateBack)
                }
            }

            is ShopEvent.SelectSkin -> selectSkin(event.skin)
        }
    }

    private fun selectSkin(skin: Skin) {
        val player = _player.value
        val updated = when {
            skin == player.equippedSkin -> return
            skin in player.ownedSkins -> player.copy(equippedSkin = skin)
            player.balance >= skin.price -> player.copy(
                balance = player.balance - skin.price,
                ownedSkins = player.ownedSkins + skin,
                equippedSkin = skin
            )

            else -> {
                viewModelScope.launch {
                    _event.emit(ShopUiEvent.NotEnoughCoins)
                }
                return
            }
        }
        storage.persistPlayer(updated)
        _player.value = updated
    }
}

sealed interface ShopEvent {
    data class SelectSkin(val skin: Skin) : ShopEvent
    data object NavigateBack : ShopEvent
}

sealed class ShopUiEvent {
    data object NavigateBack : ShopUiEvent()
    data object NotEnoughCoins : ShopUiEvent()
}
