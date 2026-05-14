package com.namma.platform.presentation.coach

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import com.namma.platform.ui.components.PlatformChip
import com.namma.platform.ui.theme.NammaBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoachLayoutScreen(
    onNavigateBack: () -> Unit,
    viewModel: CoachLayoutViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val layout = uiState.coachLayout

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(layout?.trainName ?: uiState.trainNo, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (layout != null) {
                // Header Card
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(layout.trainNo, style = MaterialTheme.typography.titleMedium)
                            Text(layout.trainName, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                        }
                        // Dummy platform chip for header
                        PlatformChip(platformNo = "TBD")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Coach Strip Section
                Text(
                    text = "ನಿಮ್ಮ ಡಬ್ಬ ಎಲ್ಲಿದೆ? / Where is your coach?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("← Engine", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    Text("Last Coach →", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }

                Spacer(modifier = Modifier.height(8.dp))

                val firstGenIndex = layout.coaches.indexOfFirst { it == "GEN" || it == "LADIES" || it == "GS" }
                val infiniteTransition = rememberInfiniteTransition(label = "bounce")
                val dy by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 10f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(500, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "bounceAnim"
                )

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    itemsIndexed(layout.coaches) { index, coachLabel ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            if (index == firstGenIndex) {
                                Box(modifier = Modifier.height(30.dp).offset(y = dy.dp)) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDownward, 
                                        contentDescription = "Stand Here",
                                        tint = NammaBlue
                                    )
                                }
                                Text("Stand Here", style = MaterialTheme.typography.labelSmall, color = NammaBlue)
                            } else {
                                Spacer(modifier = Modifier.height(46.dp))
                            }
                            
                            CoachBox(
                                coachLabel = coachLabel,
                                isHighlighted = (coachLabel == "GEN" || coachLabel == "LADIES" || coachLabel == "GS"),
                                isSelected = uiState.selectedCoachIndex == index,
                                onClick = { viewModel.onCoachTapped(index) }
                            )
                        }
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Coach layout not available.")
                }
            }
        }

        // Tooltip Dialog
        if (uiState.selectedCoachIndex != null && layout != null) {
            val coach = layout.coaches[uiState.selectedCoachIndex!!]
            CoachTooltip(coachLabel = coach, onDismiss = { viewModel.clearSelection() })
        }
    }
}

@Composable
fun CoachTooltip(coachLabel: String, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.padding(16.dp).wrapContentHeight()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Coach: $coachLabel", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                
                val description = when {
                    coachLabel == "GEN" || coachLabel == "GS" -> "ಸಾಮಾನ್ಯ ಡಬ್ಬ — ಯಾರೂ ಕೂರಬಹುದು (General — anyone can board)"
                    coachLabel == "LADIES" -> "ಮಹಿಳೆಯರ ಡಬ್ಬ — ಮಹಿಳೆಯರಿಗೆ ಮಾತ್ರ (Ladies only)"
                    coachLabel.startsWith("S") -> "Sleeper class — reserved berths"
                    coachLabel.startsWith("B") -> "AC 3-tier — reserved, air conditioned"
                    coachLabel.startsWith("A") -> "AC 2-tier — reserved, premium"
                    coachLabel.contains("Engine", ignoreCase = true) -> "ಇಂಜಿನ್ — ಪ್ರಯಾಣಿಕರಿಲ್ಲ (Engine — no passengers)"
                    else -> "Unknown coach type"
                }
                
                Text(description, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = onDismiss, modifier = Modifier.align(Alignment.End)) {
                    Text("OK")
                }
            }
        }
    }
}
