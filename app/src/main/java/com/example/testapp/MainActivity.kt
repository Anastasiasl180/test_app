package com.example.testapp

import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.testapp.navigation.NavHost
import org.koin.androidx.viewmodel.ext.android.viewModel



class MainActivity : AppCompatActivity() {

    private val globalViewModel: AppGlobalViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppLanguage.setDefaultIfNotChosen()
        setOrientation(this, ScreenOrientation.Portrait)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )
        setContent {
            NavHost()
        }
    }

    override fun onResume() {
        super.onResume()
        hideNavigationBar()
        globalViewModel.resumeAudio()
    }

    private fun hideNavigationBar() {
        WindowCompat.getInsetsController(window, window.decorView).apply {
            hide(WindowInsetsCompat.Type.navigationBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    override fun onStop() {
        super.onStop()
        globalViewModel.pauseAudio()
    }
}
