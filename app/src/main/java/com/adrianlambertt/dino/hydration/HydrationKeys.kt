package com.adrianlambertt.dino.hydration

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey

object HydrationKeys {
    val CURRENT_SIPS = intPreferencesKey("current_sips")
    val GOAL_SIPS = intPreferencesKey("goal_sips")
    val LAST_UPDATED_DAY = longPreferencesKey("last_updated_day")
}