package com.namma.platform.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.namma.platform.ui.theme.ErrorRed
import com.namma.platform.ui.theme.SuccessGreen
import com.namma.platform.ui.theme.NammaPlatformTheme

/**
 * Inline delay badge for train status.
 *
 * - **delay == 0** → green dot + "On Time"
 * - **delay > 0**  → red dot  + "{n} min late"
 *
 * @param delayMinutes Delay in minutes. Zero means on time.
 * @param modifier     Optional modifier.
 */
@Composable
fun DelayBadge(
    delayMinutes: Int,
    modifier: Modifier = Modifier
) {
    val isOnTime = delayMinutes <= 0
    val dotColor = if (isOnTime) SuccessGreen else ErrorRed
    val label = if (isOnTime) "On Time" else "$delayMinutes min late"
    val containerColor = dotColor.copy(alpha = 0.12f)

    Surface(
        modifier = modifier.semantics {
            contentDescription = if (isOnTime) "On time" else "$delayMinutes minutes late"
        },
        shape = RoundedCornerShape(20.dp),
        color = containerColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Status dot
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(dotColor)
            )

            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = dotColor
            )
        }
    }
}

// ─── Previews ────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
private fun DelayBadgeOnTimePreview() {
    NammaPlatformTheme {
        DelayBadge(delayMinutes = 0)
    }
}

@Preview(showBackground = true)
@Composable
private fun DelayBadgeLatePreview() {
    NammaPlatformTheme {
        DelayBadge(delayMinutes = 15)
    }
}

@Preview(showBackground = true)
@Composable
private fun DelayBadgeHeavyLatePreview() {
    NammaPlatformTheme {
        DelayBadge(delayMinutes = 120)
    }
}
