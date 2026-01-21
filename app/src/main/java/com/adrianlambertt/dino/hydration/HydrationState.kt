package com.adrianlambertt.dino.hydration

import android.content.Context
import androidx.datastore.preferences.core.edit
import java.time.LocalDate
import kotlinx.coroutines.flow.first
import kotlin.math.max
import kotlin.math.min

const val DEFAULT_GOAL_SIPS = 8
const val DEFAULT_CURRENT_SIPS = 0

data class HydrationState(
    val currentSips: Int,
    val goalSips: Int,
    val lastUpdatedEpochDay: Long
)

suspend fun Context.getHydrationState(): HydrationState {
    val prefs = hydrationDataStore.data.first()
    val today = LocalDate.now().toEpochDay()

    val lastUpdatedDay =
        prefs[HydrationKeys.LAST_UPDATED_DAY] ?: today

    val goalSips =
        prefs[HydrationKeys.GOAL_SIPS] ?: DEFAULT_GOAL_SIPS

    val currentSips =
        prefs[HydrationKeys.CURRENT_SIPS] ?: DEFAULT_CURRENT_SIPS

    // Reset if it's a new day
    if (lastUpdatedDay != today) {
        hydrationDataStore.edit {
            it[HydrationKeys.CURRENT_SIPS] = DEFAULT_CURRENT_SIPS
            it[HydrationKeys.LAST_UPDATED_DAY] = today
        }
    }

    return HydrationState(
        currentSips = currentSips,
        goalSips = goalSips,
        lastUpdatedEpochDay = today
    )
}

suspend fun Context.setGoalSips(goal: Int) {
    hydrationDataStore.edit { prefs ->
        prefs[HydrationKeys.GOAL_SIPS] = goal
    }
}

suspend fun Context.addSip() {
    hydrationDataStore.edit { prefs ->
        val current = prefs[HydrationKeys.CURRENT_SIPS] ?: 0
        val goal = prefs[HydrationKeys.GOAL_SIPS] ?: DEFAULT_GOAL_SIPS

        prefs[HydrationKeys.CURRENT_SIPS] = min(current + 1, goal)
        prefs[HydrationKeys.LAST_UPDATED_DAY] = LocalDate.now().toEpochDay()
    }
}

    suspend fun Context.minusSip() {
        hydrationDataStore.edit { prefs ->
            val current = prefs[HydrationKeys.CURRENT_SIPS] ?: 0
            prefs[HydrationKeys.CURRENT_SIPS] = max(current - 1, 0)
            prefs[HydrationKeys.LAST_UPDATED_DAY] = LocalDate.now().toEpochDay()
        }
}
