package com.namma.platform.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.namma.platform.ui.theme.Shimmer
import com.namma.platform.ui.theme.ShimmerHighlight
import com.namma.platform.ui.theme.NammaPlatformTheme

/**
 * Shimmer loading skeleton that mirrors the layout of a train card.
 *
 * Uses [Brush.linearGradient] with an animated horizontal offset to
 * create the classic shimmer sweep effect. The skeleton includes
 * placeholders for:
 *   - Train number & name (two lines)
 *   - Route (source → destination)
 *   - Time block & platform badge
 *   - Two action buttons
 */
@Composable
fun ShimmerCard(
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = -300f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerTranslate"
    )

    val shimmerBrush = Brush.linearGradient(
        colors = listOf(Shimmer, ShimmerHighlight, Shimmer),
        start = Offset(translateAnim, 0f),
        end = Offset(translateAnim + 300f, 0f)
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = com.namma.platform.ui.theme.SurfaceColor
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // ── Row 1: Train number + name ──
            Row(verticalAlignment = Alignment.CenterVertically) {
                ShimmerBox(
                    brush = shimmerBrush,
                    modifier = Modifier
                        .width(60.dp)
                        .height(14.dp)
                )
                Spacer(Modifier.width(8.dp))
                ShimmerBox(
                    brush = shimmerBrush,
                    modifier = Modifier
                        .weight(1f)
                        .height(14.dp)
                )
            }

            Spacer(Modifier.height(10.dp))

            // ── Row 2: Route ──
            ShimmerBox(
                brush = shimmerBrush,
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(10.dp)
            )

            Spacer(Modifier.height(16.dp))

            // ── Row 3: Time + platform badge ──
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column {
                    ShimmerBox(
                        brush = shimmerBrush,
                        modifier = Modifier
                            .width(80.dp)
                            .height(12.dp)
                    )
                    Spacer(Modifier.height(6.dp))
                    ShimmerBox(
                        brush = shimmerBrush,
                        modifier = Modifier
                            .width(60.dp)
                            .height(10.dp)
                    )
                }
                Spacer(Modifier.weight(1f))
                ShimmerBox(
                    brush = shimmerBrush,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }

            Spacer(Modifier.height(16.dp))

            // ── Row 4: Action buttons ──
            Row {
                ShimmerBox(
                    brush = shimmerBrush,
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .clip(RoundedCornerShape(20.dp))
                )
                Spacer(Modifier.width(8.dp))
                ShimmerBox(
                    brush = shimmerBrush,
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .clip(RoundedCornerShape(20.dp))
                )
            }
        }
    }
}

/**
 * Individual shimmer placeholder box.
 */
@Composable
private fun ShimmerBox(
    brush: Brush,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(brush)
    )
}

// ─── Previews ────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
private fun ShimmerCardPreview() {
    NammaPlatformTheme {
        Column {
            ShimmerCard()
            ShimmerCard()
            ShimmerCard()
        }
    }
}
