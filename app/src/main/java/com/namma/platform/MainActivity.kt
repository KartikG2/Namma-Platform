package com.namma.platform

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.namma.platform.domain.model.Station
import com.namma.platform.presentation.coach.CoachLayoutScreen
import com.namma.platform.presentation.dashboard.TrainDashboardScreen
import com.namma.platform.presentation.map.TrainMapScreen
import com.namma.platform.presentation.search.TrainSearchScreen
import com.namma.platform.presentation.search.TrainSearchViewModel
import com.namma.platform.presentation.splash.SplashScreen
import com.namma.platform.presentation.station.StationSelectionScreen
import com.namma.platform.ui.theme.NammaPlatformTheme
import com.namma.platform.util.TtsManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Main entry point of the application.
 * Hosts the NavHost with all navigation routes.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var ttsManager: TtsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            NammaPlatformTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var showTtsDialog by remember { mutableStateOf(!ttsManager.checkKannadaTtsAvailable()) }

                    if (showTtsDialog) {
                        AlertDialog(
                            onDismissRequest = { showTtsDialog = false },
                            title = { Text("ಕನ್ನಡ ಧ್ವನಿ ಡೌನ್ಲೋಡ್ ಮಾಡಿ") },
                            text = { Text("For Kannada announcements, please install Kannada Text-to-Speech") },
                            confirmButton = {
                                TextButton(onClick = {
                                    try {
                                        val intent = Intent("android.settings.TTS_SETTINGS").apply {
                                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                        }
                                        startActivity(intent)
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                    showTtsDialog = false
                                }) {
                                    Text("Install")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showTtsDialog = false }) {
                                    Text("Later")
                                }
                            }
                        )
                    }

                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = NavRoutes.SPLASH
                    ) {
                        // 1. Splash Screen
                        composable(route = NavRoutes.SPLASH) {
                            SplashScreen(
                                isTtsDialogShowing = showTtsDialog,
                                onNavigateToStationSelection = {
                                    navController.navigate(NavRoutes.TRAIN_SEARCH) {
                                        popUpTo(NavRoutes.SPLASH) { inclusive = true }
                                    }
                                }
                            )
                        }

                        // 2. Train Search Screen
                        composable(route = NavRoutes.TRAIN_SEARCH) { backStackEntry ->
                            val searchViewModel: TrainSearchViewModel = hiltViewModel()
                            
                            // Observe results from the picker reactively
                            val fromStn by backStackEntry.savedStateHandle.getStateFlow<Station?>(
                                "from_station", null
                            ).collectAsState()
                            
                            val toStn by backStackEntry.savedStateHandle.getStateFlow<Station?>(
                                "to_station", null
                            ).collectAsState()

                            LaunchedEffect(fromStn) {
                                fromStn?.let {
                                    searchViewModel.onFromStationSelected(it)
                                    backStackEntry.savedStateHandle["from_station"] = null
                                }
                            }
                            
                            LaunchedEffect(toStn) {
                                toStn?.let {
                                    searchViewModel.onToStationSelected(it)
                                    backStackEntry.savedStateHandle["to_station"] = null
                                }
                            }

                            TrainSearchScreen(
                                viewModel = searchViewModel,
                                onSelectFrom = { navController.navigate(NavRoutes.stationPicker("from")) },
                                onSelectTo = { navController.navigate(NavRoutes.stationPicker("to")) },
                                onFindTrains = { from, to ->
                                    navController.navigate(NavRoutes.trainDashboard(from, to))
                                }
                            )
                        }

                        // 3. Station Picker
                        composable(
                            route = NavRoutes.STATION_PICKER,
                            arguments = listOf(navArgument("pickerType") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val type = backStackEntry.arguments?.getString("pickerType") ?: "from"
                            StationSelectionScreen(
                                title = if (type == "from") "Boarding Station (From)" else "Destination Station (To)",
                                onStationSelected = { station ->
                                    navController.previousBackStackEntry?.savedStateHandle?.set(
                                        if (type == "from") "from_station" else "to_station",
                                        station
                                    )
                                    navController.popBackStack()
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }

                        // 4. Train Dashboard
                        composable(
                            route = NavRoutes.TRAIN_DASHBOARD,
                            arguments = listOf(
                                navArgument("fromCode") { type = NavType.StringType },
                                navArgument("toCode") { type = NavType.StringType }
                            )
                        ) {
                            TrainDashboardScreen(
                                onNavigateToCoachLayout = { trainNo ->
                                    navController.navigate(NavRoutes.coachLayout(trainNo))
                                },
                                onNavigateToMap = { trainNo, date, stationCode ->
                                    navController.navigate(NavRoutes.trainMap(trainNo, date, stationCode))
                                },
                                onNavigateBack = { navController.popBackStack() },
                                ttsManager = ttsManager
                            )
                        }

                        // 5. Coach Layout
                        composable(
                            route = NavRoutes.COACH_LAYOUT,
                            arguments = listOf(navArgument("trainNo") { type = NavType.StringType })
                        ) {
                            CoachLayoutScreen(onNavigateBack = { navController.popBackStack() })
                        }

                        // 6. Train Map
                        composable(
                            route = NavRoutes.TRAIN_MAP,
                            arguments = listOf(
                                navArgument("trainNo") { type = NavType.StringType },
                                navArgument("date") { type = NavType.StringType },
                                navArgument("stationCode") { type = NavType.StringType }
                            )
                        ) {
                            TrainMapScreen(onNavigateBack = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}

/**
 * Centralized navigation route definitions.
 */
object NavRoutes {
    const val SPLASH = "splash"
    const val TRAIN_SEARCH = "train_search"
    const val STATION_PICKER = "station_picker/{pickerType}"
    const val TRAIN_DASHBOARD = "train_dashboard/{fromCode}/{toCode}"
    const val COACH_LAYOUT = "coach_layout/{trainNo}"
    const val TRAIN_MAP = "train_map/{trainNo}/{date}/{stationCode}"

    fun stationPicker(pickerType: String) = "station_picker/$pickerType"
    fun trainDashboard(fromCode: String, toCode: String) = "train_dashboard/$fromCode/$toCode"
    fun coachLayout(trainNo: String) = "coach_layout/$trainNo"
    fun trainMap(trainNo: String, date: String, stationCode: String) = "train_map/$trainNo/$date/$stationCode"
}
