package com.adrianlambertt.dino

import android.app.Application
import android.util.Log
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.adrianlambertt.dino.utils.getRandomDelay
import com.adrianlambertt.dino.workers.MotivationReminderWorker
import com.adrianlambertt.dino.workers.WaterReminderWorker
import java.util.concurrent.TimeUnit

class Dino : Application() {
    override fun onCreate() {
        super.onCreate()
        scheduleNotifications()
    }

    private fun scheduleNotifications() {
        Log.d("MainActivity", "Scheduling Notifications")
        WorkManager.getInstance(this)
            .enqueueUniqueWork("waterReminder", ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequestBuilder<WaterReminderWorker>()
                .setInitialDelay(getRandomDelay(120, 240), TimeUnit.MINUTES)
                .build())

        WorkManager.getInstance(this)
            .enqueueUniqueWork("motivationReminder", ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequestBuilder<MotivationReminderWorker>()
                .setInitialDelay(getRandomDelay(240, 480), TimeUnit.MINUTES)
                .build())
    }
}