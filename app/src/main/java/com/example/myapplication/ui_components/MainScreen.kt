package com.example.myapplication.ui_components

import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.compose.BackHandler // ВАЖНО: Убедитесь, что этот импорт есть
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.MainViewModel
import com.example.myapplication.utils.DrawerEvents
import com.example.myapplication.utils.ListItem
import com.example.myapplication.utils.MainListItem
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    context: Context,
    mainViewModel: MainViewModel = hiltViewModel(),
    onClick: (ListItem) -> Unit
) {
    val topBarTitle = rememberSaveable { mutableStateOf("ПК") }

    val categoryHistory = rememberSaveable { mutableStateOf(listOf("ПК")) }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val mainList = mainViewModel.mainList

    LaunchedEffect(topBarTitle.value) {
        if (topBarTitle.value == "Избранные") {
            mainViewModel.getFavorites()
        } else {
            mainViewModel.getAllItemsByCategory(topBarTitle.value)
        }
    }

    BackHandler(enabled = categoryHistory.value.size > 1) {
        val newHistory = categoryHistory.value.dropLast(1)
        categoryHistory.value = newHistory


        topBarTitle.value = newHistory.last()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerMenu { event ->
                    when (event) {
                        is DrawerEvents.OnItemClick -> {
                            if (topBarTitle.value != event.title) {
                                topBarTitle.value = event.title
                                categoryHistory.value = categoryHistory.value + event.title
                            }
                        }
                    }
                    scope.launch { drawerState.close() }
                }
            }
        },
        content = {
            Scaffold(
                topBar = {
                    MainTopBar(
                        title = topBarTitle.value,
                        drawerState = drawerState
                    ) {
                        if (topBarTitle.value != "Избранные") {
                            topBarTitle.value = "Избранные"
                            categoryHistory.value = categoryHistory.value + "Избранные"
                        }
                    }
                }
            ) { innerPadding ->
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    items(mainList.value) { item ->
                        MainListItem(item = item) { listItem -> onClick(listItem) }
                    }
                }
            }
        }
    )
}