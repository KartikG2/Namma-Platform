package com.namma.platform.ui.theme

import androidx.compose.ui.graphics.Color

// ─── Primary Palette (Premium Orange) ───
val PrimaryOrange = Color(0xFFE25B2D)
val PrimaryOrangeDark = Color(0xFFC44A20)
val PrimaryOrangeLight = Color(0xFFF2815A)

// ─── Accent & Status ───
val AccentGreen = Color(0xFF4CAF50) // For available seats/success
val AccentGrey = Color(0xFFD3D3D3) // For unavailable/neutral
val ErrorRed = Color(0xFFD32F2F)

// ─── Background & Surface ───
val BackgroundColor = Color(0xFFFAFAFA)
val SurfaceColor = Color(0xFFFFFFFF)
val CardBackground = Color(0xFFFFFFFF)

// ─── Text ───
val TextPrimary = Color(0xFF222222)
val TextSecondary = Color(0xFF757575)
val TextTertiary = Color(0xFFBDBDBD)

// ─── Coach Colors ───
val CoachBlue = Color(0xFF1976D2)
val CoachYellow = Color(0xFFFDD835)
val CoachGreen = AccentGreen
val CoachRed = ErrorRed
val CoachGrey = AccentGrey

// ─── Misc ───
val Divider = Color(0xFFEEEEEE)
val Shimmer = Color(0xFFF0F0F0)
val ShimmerHighlight = Color(0xFFFAFAFA)

// ═══════════════════════════════════════════════
// Backward-compatible aliases replacing the old blue
// ═══════════════════════════════════════════════
val NammaBlue = PrimaryOrange
val NammaBlueDark = PrimaryOrangeDark
val NammaBlueLight = PrimaryOrangeLight
val NammaOrange = PrimaryOrange
val TrainOnTime = AccentGreen
val TrainLate = ErrorRed
val TrainDelayed = Color(0xFFFF9800)
val PlatformHighlight = PrimaryOrange
val NammaTealDark = TextPrimary
val PlatformYellow = PrimaryOrangeLight
val PrimaryBlue = PrimaryOrange
val PrimaryBlueLight = PrimaryOrangeLight
val PrimaryBlueDark = PrimaryOrangeDark
val YellowLight = PrimaryOrangeLight
val SuccessGreen = AccentGreen
val OfflineBannerAmber = TrainDelayed
