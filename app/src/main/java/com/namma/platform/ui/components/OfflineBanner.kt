package com.namma.platform.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.namma.platform.ui.theme.OfflineBannerAmber
import com.namma.platform.ui.theme.NammaPlatformTheme

/**
 * Amber offline banner with Kannada text.
 *
 * Displays "ಇಂಟರ್ನೆಟ್ ಇಲ್ಲ — ಹಳೆಯ ಮಾಹಿತಿ ತೋರಿಸಲಾಗುತ್ತಿದೆ"
 * (No internet — showing old data) with a smooth slide + fade
 * animation via [AnimatedVisibility].
 *
 * @param visible  Whether the banner should be shown.
 * @param modifier Optional modifier.
 */
@Composable
fun OfflineBanner(
    visible: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
        exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut(),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(OfflineBannerAmber)
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .semantics {
                    contentDescription =
                        "No internet connection. Showing cached data."
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.WifiOff,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(18.dp)
                    .padding(end = 0.dp)
            )

            Text(
                text = "  ಇಂಟರ್ನೆಟ್ ಇಲ್ಲ — ಹಳೆಯ ಮಾಹಿತಿ ತೋರಿಸಲಾಗುತ್ತಿದೆ",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Color.White
            )
        }
    }
}

// ─── Previews ────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
private fun OfflineBannerVisiblePreview() {
    NammaPlatformTheme {
        OfflineBanner(visible = true)
    }
}

@Preview(showBackground = true)
@Composable
private fun OfflineBannerHiddenPreview() {
    NammaPlatformTheme {
        OfflineBanner(visible = false)
    }
}
