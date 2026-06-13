package com.example.budgetwise.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.budgetwise.ui.domain.model.FontSize
import com.example.budgetwise.ui.domain.model.ThemeMode
import com.example.budgetwise.ui.screens.MainScreen
import com.example.budgetwise.ui.screens.SettingsScreen
import com.example.budgetwise.ui.screens.TransactionsScreen
import com.example.budgetwise.ui.screens.UpcomingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(navController: NavHostController,
                  currentThemeMode: ThemeMode,
                  onThemeModeChange: (ThemeMode) -> Unit,
                  currentFontSize: FontSize,
                  onFontSizeChange: (FontSize) -> Unit) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Budget Wise") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.background,
                    navigationIconContentColor = MaterialTheme.colorScheme.background,
                    actionIconContentColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            Column {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 24.dp, start = 4.dp, end = 4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    IconButton(
                        onClick = {
                            if(currentRoute != "home") {
                                navController.navigate("home") {
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Dashboard,
                            contentDescription = "Home",
                            tint = MaterialTheme.colorScheme.onSurface)
                    }
                    IconButton(
                        onClick = {
                            if(currentRoute != "transactions") {
                                navController.navigate("transactions") {
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Receipt,
                            contentDescription = "Transactions",
                            tint = MaterialTheme.colorScheme.onSurface)
                    }
                    IconButton(
                        onClick = {
                            if(currentRoute != "upcoming") {
                                navController.navigate("upcoming") {
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Event,
                            contentDescription = "Upcoming",
                            tint = MaterialTheme.colorScheme.onSurface)
                    }
                    IconButton(
                        onClick = {
                            if (currentRoute != "settings") {
                                navController.navigate("settings") {
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Settings",
                            tint = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                MainScreen()
            }
            composable("transactions") {
                TransactionsScreen()
            }
            composable("upcoming") {
                UpcomingScreen()
            }
            composable("settings") {
                SettingsScreen(
                    selectedTheme = currentThemeMode,
                    onThemeSelected = onThemeModeChange,
                    currentFontSize = currentFontSize,
                    onFontSizeChange = onFontSizeChange
                )
            }
        }
    }
}