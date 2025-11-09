package com.example.lalalikalogistics

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.authDataStore by preferencesDataStore("auth_cache")
val Context.settingsDataStore by preferencesDataStore("app_settings")