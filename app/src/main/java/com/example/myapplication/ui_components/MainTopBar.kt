package com.example.myapplication.ui_components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import com.example.myapplication.utils.AppTheme
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MainTopBar(
    title: String,
    drawerState: DrawerState,
    currentTheme: AppTheme,
    onThemeChange: (AppTheme) -> Unit,
    onFavClick: () -> Unit
) {
    val coroutine = rememberCoroutineScope()
    var showThemeMenu by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = { coroutine.launch { drawerState.open() } }) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Меню")
            }
        },
        actions = {
            // Кнопка смены темы
            IconButton(onClick = { showThemeMenu = true }) {
                Icon(imageVector = Icons.Default.Settings, contentDescription = "Тема")
            }
            DropdownMenu(
                expanded = showThemeMenu,
                onDismissRequest = { showThemeMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("🌞 Светлая") },
                    onClick = { onThemeChange(AppTheme.LIGHT); showThemeMenu = false }
                )
                DropdownMenuItem(
                    text = { Text("🌙 Тёмная") },
                    onClick = { onThemeChange(AppTheme.DARK); showThemeMenu = false }
                )
                DropdownMenuItem(
                    text = { Text("⚙️ Системная") },
                    onClick = { onThemeChange(AppTheme.SYSTEM); showThemeMenu = false }
                )
            }

            // Кнопка избранного
            IconButton(onClick = { onFavClick() }) {
                Icon(imageVector = Icons.Default.Favorite, contentDescription = "Избранное")
            }
        }
    )
}