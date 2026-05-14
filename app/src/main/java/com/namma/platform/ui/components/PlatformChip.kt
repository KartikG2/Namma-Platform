package com.namma.platform.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
import com.namma.platform.ui.theme.PlatformYellow
import com.namma.platform.ui.theme.PrimaryBlue
import com.namma.platform.ui.theme.NammaPlatformTheme

/**
 * Platform number chip styled after Indian Railway station display boards.
 *
 * @param platformNo Platform number to display (e.g. "1", "2", "TBD"…)
 * @param modifier   Optional modifier for external layout control.
 */
@Composable
fun PlatformChip(
    platformNo: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(PlatformYellow)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .semantics {
                contentDescription = "Platform $platformNo"
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = platformNo,
            style = MaterialTheme.typography.displayLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = PrimaryBlue
        )
    }
}

// ─── Previews ────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
private fun PlatformChipPreview() {
    NammaPlatformTheme {
        PlatformChip(platformNo = "3")
    }
}

@Preview(showBackground = true)
@Composable
private fun PlatformChipTwoDigitPreview() {
    NammaPlatformTheme {
        PlatformChip(platformNo = "12")
    }
}
