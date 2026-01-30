package com.adrianlambertt.dino.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.preference.PreferenceManager
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.adrianlambertt.dino.MainActivity
import com.adrianlambertt.dino.R
import com.adrianlambertt.dino.utils.getRandomDelay
import java.util.concurrent.TimeUnit

class MotivationReminderWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        val context = applicationContext

        val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val enabled = prefs.getBoolean("appreciation_notifications", true)

        // Fail out early if not permitted in settings
        if (!enabled) return Result.success()

        val messages = listOf(
            "Take a deep breath 😊",
            "You’re doing great — keep going!",
            "Dino hopes you're having a great day!",
            "Take life one step at a time",
            "Stretch your body for 10 seconds",
            "Remember to have time for yourself",
            "Your comfy bed can wait, your goals cannot",
            "Dino wants you to stay safe!",
            "Good vibes only today!",
            "Remember to do your hobbies!"
        )


        showNotification(context, "Dino Reminder", messages.random())
        Log.d("MotivationReminderWorker", "Worker running!")
        scheduleNext(context)
        return Result.success()
    }

    private fun scheduleNext(context: Context) {
        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                "motivationReminder",
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequestBuilder<MotivationReminderWorker>()
                    .setInitialDelay(getRandomDelay(240, 480), TimeUnit.MINUTES)
                    .build()
            )
    }

    private fun showNotification(context: Context, title: String, text: String) {
        val channelId = "dino_notifications"


        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create channel for Android 8+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Dino Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify((0..9999).random(), notification)
    }
}