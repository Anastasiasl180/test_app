package com.example.testapp.ui.webview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel


@KoinViewModel
class WebViewViewModel : ViewModel() {

    val url: String = "http://lk.nsq.market/ru/tools/testing"

    private val _event = MutableSharedFlow<WebViewUiEvent>()
    val event = _event.asSharedFlow()

    fun onEvent(event: WebViewEvent) {
        when (event) {
            WebViewEvent.NavigateBack -> {
                viewModelScope.launch {
                    _event.emit(WebViewUiEvent.NavigateBack)
                }
            }
        }
    }
}

sealed interface WebViewEvent {
    data object NavigateBack : WebViewEvent
}

sealed class WebViewUiEvent {
    data object NavigateBack : WebViewUiEvent()
}
