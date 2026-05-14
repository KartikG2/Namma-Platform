package com.namma.platform.presentation.search

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.namma.platform.domain.model.SearchHistoryItem
import com.namma.platform.domain.model.Station

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainSearchScreen(
    onSelectFrom: () -> Unit,
    onSelectTo: () -> Unit,
    onFindTrains: (String, String) -> Unit,
    viewModel: TrainSearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ರೈಲು ಹುಡುಕಾಟ / Train Search", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color(0xFF1A1A1A)
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFFF5F5F5)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Main Search Card
            SearchCard(
                fromStation = uiState.fromStation,
                toStation = uiState.toStation,
                onSelectFrom = onSelectFrom,
                onSelectTo = onSelectTo,
                onClearFrom = { viewModel.clearFromStation() },
                onClearTo = { viewModel.clearToStation() },
                onSwap = { viewModel.swapStations() },
                onFindTrains = { viewModel.onFindTrainsClicked(onFindTrains) }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Search History Section
            if (uiState.searchHistory.isNotEmpty()) {
                Text(
                    text = "SEARCH HISTORY / ಇತಿಹಾಸ",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFF757575),
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(bottom = 12.dp, start = 4.dp)
                )
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column {
                        uiState.searchHistory.forEachIndexed { index, item ->
                            HistoryItemRow(item) {
                                viewModel.onHistoryItemTapped(item)
                            }
                            if (index < uiState.searchHistory.size - 1) {
                                HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 0.5.dp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchCard(
    fromStation: Station?,
    toStation: Station?,
    onSelectFrom: () -> Unit,
    onSelectTo: () -> Unit,
    onClearFrom: () -> Unit,
    onClearTo: () -> Unit,
    onSwap: () -> Unit,
    onFindTrains: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Column {
                // FROM Input
                StationInputRow(
                    label = "ಎಲ್ಲಿಂದ? / From",
                    station = fromStation,
                    onClick = onSelectFrom,
                    onClear = onClearFrom,
                    isFrom = true
                )

                // Dotted Line and Swap Button Area
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {
                    // Vertical Dotted Line
                    Canvas(
                        modifier = Modifier
                            .padding(start = 12.dp) // Align with ○ icon center
                            .width(1.dp)
                            .fillMaxHeight()
                    ) {
                        drawLine(
                            color = Color(0xFF9E9E9E),
                            start = Offset(0f, 0f),
                            end = Offset(0f, size.height),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f),
                            strokeWidth = 2.dp.toPx()
                        )
                    }

                    // Swap Button
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 8.dp)
                    ) {
                        IconButton(
                            onClick = onSwap,
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.White, CircleShape)
                                .border(1.dp, Color(0xFFEEEEEE), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SwapVert,
                                contentDescription = "Swap",
                                tint = Color(0xFF2E7D32)
                            )
                        }
                    }
                }

                // TO Input
                StationInputRow(
                    label = "ಎಲ್ಲಿಗೆ? / To",
                    station = toStation,
                    onClick = onSelectTo,
                    onClear = onClearTo,
                    isFrom = false
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Find Trains Button
                Button(
                    onClick = onFindTrains,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2E7D32),
                        disabledContainerColor = Color.LightGray
                    ),
                    enabled = fromStation != null && toStation != null
                ) {
                    Text(
                        text = "ರೈಲುಗಳನ್ನು ಹುಡುಕಿ / Find trains",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun StationInputRow(
    label: String,
    station: Station?,
    onClick: () -> Unit,
    onClear: () -> Unit,
    isFrom: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Circle Icon
        Canvas(modifier = Modifier.size(24.dp)) {
            drawCircle(
                color = if (isFrom) Color(0xFF9E9E9E) else Color(0xFF2E7D32),
                radius = 6.dp.toPx(),
                style = Stroke(width = 2.dp.toPx())
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        if (station != null) {
            // Station Code Badge
            Surface(
                color = Color(0xFF1565C0),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.padding(end = 12.dp)
            ) {
                Text(
                    text = station.code,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }

            Text(
                text = station.nameEnglish,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1A1A1A),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = onClear) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Clear",
                    tint = Color(0xFFBDBDBD),
                    modifier = Modifier.size(20.dp)
                )
            }
        } else {
            Text(
                text = label,
                fontSize = 16.sp,
                color = Color(0xFFBDBDBD),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun HistoryItemRow(item: SearchHistoryItem, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { onClick() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = if (item.trainNo.isNotEmpty()) "${item.trainNo} - ${item.trainName}" else "Recent Search",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${item.fromStation.code} → ${item.toStation.code}",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF757575)
            )
        }
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = Color(0xFFBDBDBD)
        )
    }
}
