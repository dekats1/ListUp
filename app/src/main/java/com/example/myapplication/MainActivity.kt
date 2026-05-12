package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui_components.InfoScreen
import com.example.myapplication.ui_components.MainScreen
import com.example.myapplication.utils.AppTheme
import com.example.myapplication.utils.ItemSaver
import com.example.myapplication.utils.ListItem
import com.example.myapplication.utils.Routes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val themeViewModel: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val theme by themeViewModel.currentTheme.collectAsState()
            val isSystemDark = isSystemInDarkTheme()

            val darkTheme = when (theme) {
                AppTheme.DARK   -> true
                AppTheme.LIGHT  -> false
                AppTheme.SYSTEM -> isSystemDark
            }

            MyApplicationTheme(darkTheme = darkTheme) {
                var item = rememberSaveable(stateSaver = ItemSaver) {
                    mutableStateOf(
                        ListItem(id = 0, title = "", imageName = "", htmlName = "", isfav = false, category = "")
                    )
                }
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = Routes.MAIN_SCREEN.route) {
                    composable(Routes.MAIN_SCREEN.route) {
                        MainScreen(
                            context = this@MainActivity,
                            onItemClick = { listItem ->
                                item.value = listItem.copy()
                                navController.navigate(Routes.INFO_SCREEN.route)
                            },
                            currentTheme = theme,
                            onThemeChange = { themeViewModel.setTheme(it) }
                        )
                    }
                    composable(Routes.INFO_SCREEN.route) {
                        InfoScreen(item = item.value)
                    }
                }
            }
        }
    }
}