package com.example.budgetwise.ui.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.budgetwise.ui.domain.models.FontSize
import com.example.budgetwise.ui.domain.models.ThemeMode
import com.example.budgetwise.ui.screens.MainScreen
import com.example.budgetwise.ui.screens.SettingsScreen
import com.example.budgetwise.ui.screens.TransactionsScreen
import com.example.budgetwise.ui.screens.UpcomingScreen
import com.example.budgetwise.ui.screens.addTransaction.AddTransactionScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(navController: NavHostController,
                  currentThemeMode: ThemeMode,
                  onThemeModeChange: (ThemeMode) -> Unit,
                  currentFontSize: FontSize,
                  onFontSizeChange: (FontSize) -> Unit) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val showBottomSheet = remember { mutableStateOf(false) }
    val ICON_SIZE = 32.dp
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
                    horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
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
                            tint = if(currentRoute == "home")
                                MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(ICON_SIZE)
                        )
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
                            tint = if(currentRoute == "transactions")
                                MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(ICON_SIZE)
                        )
                    }
                    IconButton(
                        onClick = { showBottomSheet.value = true }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(ICON_SIZE)
                        )
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
                            tint = if(currentRoute == "upcoming")
                                MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(ICON_SIZE)
                        )
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
                            tint = if(currentRoute == "settings")
                                MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(ICON_SIZE)
                        )
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
            composable("home") { MainScreen() }
            composable("transactions") { TransactionsScreen() }
            composable("upcoming") { UpcomingScreen() }
            composable("settings") {
                SettingsScreen(
                    selectedTheme = currentThemeMode,
                    onThemeSelected = onThemeModeChange,
                    currentFontSize = currentFontSize,
                    onFontSizeChange = onFontSizeChange
                )
            }
            composable("add_transaction/{type}") { backStackEntry ->
                val transactionType = backStackEntry.arguments?.getString("type")
                AddTransactionScreen(transactionType, context = LocalContext.current)
            }
        }
        if(showBottomSheet.value) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet.value = false },
                sheetState = bottomSheetState
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Select Action",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ListItem(
                        headlineContent = { Text("Add Income") },
                        leadingContent = {
                            Icon(
                                Icons.AutoMirrored.Filled.TrendingUp,
                                tint = MaterialTheme.colorScheme.primary,
                                contentDescription = null)
                         },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showBottomSheet.value = false
                                navController.navigate("add_transaction/incoming")
                            }
                    )
                    ListItem(
                        headlineContent = { Text("Add Expense") },
                        leadingContent = {
                            Icon(
                                Icons.AutoMirrored.Filled.TrendingDown,
                                tint = MaterialTheme.colorScheme.error,
                                contentDescription = null)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showBottomSheet.value = false
                                navController.navigate("add_transaction/outgoing")
                            }
                    )
                }
            }
        }
    }
}