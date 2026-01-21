package com.adrianlambertt.dino.hydration

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.core.DataStore

val Context.hydrationDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "hydration_prefs"
)