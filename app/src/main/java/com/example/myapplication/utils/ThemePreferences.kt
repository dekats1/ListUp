package com.example.myapplication.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "settings")

enum class AppTheme { SYSTEM, LIGHT, DARK }

class ThemePreferences(private val context: Context) {

    companion object {
        val THEME_KEY = stringPreferencesKey("app_theme")
    }

    val themeFlow: Flow<AppTheme> = context.dataStore.data.map { prefs ->
        when (prefs[THEME_KEY]) {
            "LIGHT" -> AppTheme.LIGHT
            "DARK"  -> AppTheme.DARK
            else    -> AppTheme.SYSTEM
        }
    }

    suspend fun saveTheme(theme: AppTheme) {
        context.dataStore.edit { it[THEME_KEY] = theme.name }
    }
}