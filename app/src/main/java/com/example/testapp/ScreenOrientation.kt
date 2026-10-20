package com.example.testapp

import android.content.Context

sealed class ScreenOrientation(val code: Int) {
    data object Portrait : ScreenOrientation(1)
    data object Landscape : ScreenOrientation(0)
}

fun setOrientation(context: Context, orientation: ScreenOrientation) {
    context::class.java.methods.find {
        it.name == "setRequestedOrientation"
    }?.invoke(context, orientation.code)
}
