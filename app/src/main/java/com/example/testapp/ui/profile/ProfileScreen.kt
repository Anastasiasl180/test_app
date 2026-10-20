package com.example.testapp.ui.profile

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import com.example.testapp.R
import com.example.testapp.ui.ui_templates.NContainer
import com.example.testapp.ui.ui_templates.NSnackbarHost
import com.example.testapp.ui.profile.ui_components.AchievementsList
import com.example.testapp.ui.profile.ui_components.AchievementsTitle
import com.example.testapp.ui.profile.ui_components.ProfileAvatar
import com.example.testapp.ui.profile.ui_components.ProfileName
import com.example.testapp.ui.profile.ui_components.ProfileStats
import com.example.testapp.ui.ui_templates.showShortSnackbar
import java.io.File
import kotlinx.coroutines.launch


@Composable
fun ProfileScreen(viewModel: ProfileViewModel, snackbarHostState: SnackbarHostState) {
    val player by viewModel.player
    val ctx = LocalContext.current
    val activity = LocalActivity.current as ComponentActivity
    val scope = rememberCoroutineScope()
    val noPhotoText = stringResource(R.string.profile_no_photo)
    val cameraDeniedText = stringResource(R.string.profile_camera_denied)

    val snapshotLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bmp: Bitmap? ->
        if (bmp != null) {
            val path = saveBitmap(ctx, bmp)
            if (path != null) viewModel.onEvent(ProfileEvent.ChangeAvatar(path))
        } else {
            scope.launch { snackbarHostState.showShortSnackbar(noPhotoText) }
        }
    }

    val settingsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            snapshotLauncher.launch(null)
        } else {
            scope.launch { snackbarHostState.showShortSnackbar(cameraDeniedText) }
        }
    }

    val permLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        when {
            granted -> snapshotLauncher.launch(null)
            activity.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) ->
                scope.launch { snackbarHostState.showShortSnackbar(cameraDeniedText) }
            else -> settingsLauncher.launch(
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", ctx.packageName, null))
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        NContainer(
            action = { viewModel.onEvent(ProfileEvent.NavigateBack) },
            leftImage = R.drawable.icon_back,
            balance = player.balance,
            content = {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.1f)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.14f),
                        contentAlignment = Alignment.Center
                    ) {
                        ProfileAvatar(
                            photoUri = player.photoUri,
                            onClick = { permLauncher.launch(Manifest.permission.CAMERA) }
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.08f),
                        contentAlignment = Alignment.Center
                    ) {
                        ProfileName(
                            name = player.nickname.orEmpty(),
                            level = viewModel.currentLevel,
                            onNameChange = { viewModel.onEvent(ProfileEvent.ChangeName(it)) }
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.1f),
                        contentAlignment = Alignment.Center
                    ) {
                        ProfileStats(
                            levels = viewModel.levelsPlayed,
                            bestScore = viewModel.bestScore,
                            coins = player.balance
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.05f),
                        contentAlignment = Alignment.Center
                    ) {
                        AchievementsTitle()
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.3f),
                        contentAlignment = Alignment.Center
                    ) {
                        AchievementsList(
                            achievements = viewModel.allAchievements,
                            unlocked = viewModel.unlockedAchievements
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.23f)
                    )
                }
            }
        )

        NSnackbarHost(hostState = snackbarHostState)
    }
}

private fun saveBitmap(context: Context, bitmap: Bitmap): String? = try {
    val name = "avatar_${System.currentTimeMillis()}.jpg"
    context.openFileOutput(name, Context.MODE_PRIVATE).use {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, it)
    }
    Uri.fromFile(File(context.filesDir, name)).toString()
} catch (_: Exception) {
    null
}
