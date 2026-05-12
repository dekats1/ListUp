package com.example.myapplication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.utils.AppTheme
import com.example.myapplication.utils.ThemePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val prefs = ThemePreferences(application)

    val currentTheme = prefs.themeFlow.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        AppTheme.SYSTEM
    )

    fun setTheme(theme: AppTheme) = viewModelScope.launch {
        prefs.saveTheme(theme)
    }
}