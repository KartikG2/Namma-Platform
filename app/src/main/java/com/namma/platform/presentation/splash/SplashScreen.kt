package com.namma.platform.presentation.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.namma.platform.ui.theme.NammaBlue
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    isTtsDialogShowing: Boolean = false,
    onNavigateToStationSelection: () -> Unit
) {
    LaunchedEffect(key1 = isTtsDialogShowing) {
        if (!isTtsDialogShowing) {
            delay(1500)
            onNavigateToStationSelection()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NammaBlue),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Train,
                contentDescription = "Train Icon",
                modifier = Modifier.size(100.dp),
                tint = Color.White
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "ನಮ್ಮ ಪ್ಲಾಟ್ಫಾರ್ಮ್",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Namma Platform",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "ನಿಮ್ಮ ರೈಲು ಎಲ್ಲಿದೆ? / Where is your train?",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}
