package com.namma.platform.presentation.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import com.namma.platform.domain.model.Train
import com.namma.platform.ui.components.OfflineBanner
import com.namma.platform.ui.components.ShimmerCard
import com.namma.platform.ui.theme.NammaBlue
import com.namma.platform.ui.theme.PlatformYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainDashboardScreen(
    onNavigateBack: () -> Unit,
    onNavigateToCoachLayout: (String) -> Unit,
    onNavigateToMap: (String, String, String) -> Unit,
    ttsManager: com.namma.platform.util.TtsManager,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(uiState.selectedStation?.nameKannada ?: uiState.stationCode, fontWeight = FontWeight.Bold) 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.onRefresh() }) {
                        Icon(Icons.Default.Refresh, "Refresh")
                    }
                }
            )
        },
        floatingActionButton = {
            if (uiState.trains.isNotEmpty()) {
                ExtendedFloatingActionButton(
                    onClick = { 
                        uiState.trains.firstOrNull()?.let { train ->
                            viewModel.onHelpMeTapped(train, if(train.platformNo > 0) train.platformNo else 1, ttsManager)
                        }
                    },
                    icon = { Icon(Icons.Default.Mic, "Help Me") },
                    text = { Text("ಸಹಾಯ ಮಾಡಿ / Help Me", fontWeight = FontWeight.Bold) },
                    containerColor = PlatformYellow,
                    contentColor = Color.Black,
                    shape = RoundedCornerShape(16.dp),
                    elevation = FloatingActionButtonDefaults.elevation(8.dp)
                )
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            OfflineBanner(visible = uiState.isOffline)

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                if (uiState.isLoading && uiState.trains.isEmpty()) {
                    LazyColumn(contentPadding = PaddingValues(16.dp)) {
                        items(3) {
                            ShimmerCard(modifier = Modifier.padding(bottom = 12.dp))
                        }
                    }
                } else if (uiState.trains.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No trains available")
                    }
                } else {
                    LazyColumn(contentPadding = PaddingValues(16.dp)) {
                        items(uiState.trains, key = { it.trainNo }) { train ->
                            TrainCard(
                                train = train,
                                onTrackTapped = { onNavigateToMap(train.trainNo, "today", uiState.stationCode) },
                                onClick = { onNavigateToCoachLayout(train.trainNo) }
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }
        }
    }
}
