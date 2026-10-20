package com.example.testapp.notifications

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.testapp.MainActivity
import com.example.testapp.R
import com.example.testapp.storage.AppStorage
import org.koin.core.annotation.Single
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Calendar
import java.util.concurrent.TimeUnit

const val DAILY_REWARD_COINS = 100
const val REMINDER_HOUR = 10

private const val WORK_NAME = "daily_reward_reminder"
private const val CHANNEL_ID = "daily_reward_alerts"
private const val OLD_CHANNEL_ID = "daily_reward"
private const val NOTIFICATION_ID = 1

fun dailyRewardTimeLeft(lastClaim: Long, now: Long = System.currentTimeMillis()): Long =
    if (lastClaim >= startOfDay(now)) startOfDay(now, plusDays = 1) - now else 0L

private fun startOfDay(timeMs: Long, plusDays: Int = 0): Long = Calendar.getInstance().run {
    timeInMillis = timeMs
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
    add(Calendar.DAY_OF_YEAR, plusDays)
    timeInMillis
}

@Single
class DailyRewardScheduler(private val context: Context) {

    fun scheduleReminder() {
        val now = System.currentTimeMillis()
        val remindAt = startOfDay(now, plusDays = 1) + TimeUnit.HOURS.toMillis(REMINDER_HOUR.toLong())
        val request = OneTimeWorkRequestBuilder<DailyRewardWorker>()
            .setInitialDelay(remindAt - now, TimeUnit.MILLISECONDS)
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(WORK_NAME, ExistingWorkPolicy.REPLACE, request)
    }
}

class DailyRewardWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params), KoinComponent {

    private val storage: AppStorage by inject()

    override suspend fun doWork(): Result {
        if (storage.loadSettings().notificationsEnabled) showNotification(applicationContext)
        return Result.success()
    }
}

private fun showNotification(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
        ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) !=
        PackageManager.PERMISSION_GRANTED
    ) return

    val manager = NotificationManagerCompat.from(context)
    manager.deleteNotificationChannel(OLD_CHANNEL_ID)
    manager.createNotificationChannel(
        NotificationChannelCompat.Builder(CHANNEL_ID, NotificationManagerCompat.IMPORTANCE_HIGH)
            .setName(context.getString(R.string.notification_channel))
            .setVibrationEnabled(true)
            .build()
    )

    val openApp = PendingIntent.getActivity(
        context,
        0,
        Intent(context, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP),
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )
    val notification = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(context.getString(R.string.notification_title))
        .setContentText(context.getString(R.string.notification_text, DAILY_REWARD_COINS))
        .setContentIntent(openApp)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setCategory(NotificationCompat.CATEGORY_REMINDER)
        .setDefaults(NotificationCompat.DEFAULT_ALL)
        .setAutoCancel(true)
        .build()
    manager.notify(NOTIFICATION_ID, notification)
}
