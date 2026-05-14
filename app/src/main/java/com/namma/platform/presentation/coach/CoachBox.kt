package com.namma.platform.presentation.coach

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.namma.platform.ui.theme.NammaBlue
import com.namma.platform.ui.theme.PlatformYellow

@Composable
fun CoachBox(
    coachLabel: String,
    isHighlighted: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(targetValue = if (isSelected) 0.95f else 1f, label = "scale")
    
    val infiniteTransition = rememberInfiniteTransition(label = "coach_bob")
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -4f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offsetY"
    )
    
    val isEngine = coachLabel.contains("Engine", ignoreCase = true)
    val bgColor = if (isEngine) Color(0xFFE25B2D) else if (isHighlighted) PlatformYellow else NammaBlue
    val textColor = if (isHighlighted && !isEngine) Color.Black else Color.White
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.offset(y = offsetY.dp).scale(scale).clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .width(72.dp)
                .height(100.dp)
                .clip(RoundedCornerShape(topStart = if(isEngine) 24.dp else 8.dp, topEnd = 8.dp, bottomStart = 8.dp, bottomEnd = 8.dp))
                .background(bgColor)
                .then(
                    if (isSelected) Modifier.border(2.dp, Color.White, RoundedCornerShape(8.dp))
                    else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isEngine) "🚂" else coachLabel,
                color = textColor,
                fontWeight = FontWeight.Bold,
                fontSize = if(isEngine) 32.sp else 16.sp
            )
        }
        Row(
            modifier = Modifier.width(60.dp).padding(top = 2.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(Color.Gray))
            Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(Color.Gray))
        }
    }
}
