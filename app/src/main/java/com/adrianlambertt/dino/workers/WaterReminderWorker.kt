package com.adrianlambertt.dino.workers
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.preference.PreferenceManager
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.adrianlambertt.dino.R
import com.adrianlambertt.dino.utils.getRandomDelay
import java.util.concurrent.TimeUnit

class WaterReminderWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        val context = applicationContext

        val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val enabled = prefs.getBoolean("drink_water_notifications", true)

        // Fail out early if not permitted in settings
        if (!enabled) return Result.success()

        val messages = listOf(
            "Drink some water! 💧",
            "Stay hydrated!",
            "Small sips = big health!",
            "Dino is drinking water!",
            "Hydration Reminder!",
            "Dino wants you to drink!",
            "Dino is thirsty, are you?",
            "Dino is tired... coffee time?",
            "Drink reminder"
        )

        showNotification(context, "Dino Reminder", messages.random())
        Log.d("WaterReminderWorker", "Worker running!")
        scheduleNext(context)
        return Result.success()
    }

    private fun scheduleNext(context: Context) {
        val request = OneTimeWorkRequestBuilder<WaterReminderWorker>()
            .setInitialDelay(getRandomDelay(120, 240), TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(context).enqueue(request)
    }

    private fun showNotification(context: Context, title: String, text: String) {
        val channelId = "dino_notifications"
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create channel for Android 8+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Dino Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.drawable.notification_icon)
            .setAutoCancel(true)
            .build()

        notificationManager.notify((0..9999).random(), notification)
    }
}