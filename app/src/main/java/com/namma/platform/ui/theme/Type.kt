package com.namma.platform.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ─── Font Families ───────────────────────────────────────
//
// To use the custom fonts, place the following .ttf files
// into  app/src/main/res/font/  :
//
//   noto_sans_kannada_regular.ttf
//   noto_sans_kannada_bold.ttf
//   roboto_regular.ttf
//   roboto_medium.ttf
//   roboto_mono_regular.ttf
//   roboto_mono_medium.ttf
//
// Then uncomment the Font(...) lines below and comment out
// the FontFamily.Default fallbacks.
// ──────────────────────────────────────────────────────────

// TODO: Replace with Font(R.font.noto_sans_kannada_regular) etc.
val NotoSansKannada = FontFamily.Default

// Roboto is the system default on Android
val Roboto = FontFamily.Default

// TODO: Replace with Font(R.font.roboto_mono_regular) etc.
val RobotoMono = FontFamily.Default

// ─── Typography Scale ────────────────────────────────────

val NammaTypography = Typography(

    // ── Platform Number (large station display boards) ──
    displayLarge = TextStyle(
        fontFamily = NotoSansKannada,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),

    displayMedium = TextStyle(
        fontFamily = NotoSansKannada,
        fontWeight = FontWeight.Bold,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    ),

    displaySmall = TextStyle(
        fontFamily = NotoSansKannada,
        fontWeight = FontWeight.SemiBold,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),

    // ── Headlines (Kannada train / station names) ──
    headlineLarge = TextStyle(
        fontFamily = NotoSansKannada,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),

    // Train name in Kannada
    headlineMedium = TextStyle(
        fontFamily = NotoSansKannada,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),

    headlineSmall = TextStyle(
        fontFamily = NotoSansKannada,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),

    // ── Titles (English, Roboto) ──
    titleLarge = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),

    // Train name in English
    titleMedium = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),

    titleSmall = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),

    // ── Body (Kannada station names) ──
    bodyLarge = TextStyle(
        fontFamily = NotoSansKannada,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),

    // Station names in Kannada
    bodyMedium = TextStyle(
        fontFamily = NotoSansKannada,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),

    bodySmall = TextStyle(
        fontFamily = NotoSansKannada,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),

    // ── Labels ──
    labelLarge = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),

    labelMedium = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),

    // Train number code (monospace)
    labelSmall = TextStyle(
        fontFamily = RobotoMono,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)
