package com.andyc.core.presentation.ui

import java.util.Locale
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.time.Duration

fun Duration.formatted(): String {
    val totalSeconds = inWholeSeconds
    val hours = String.format(Locale.getDefault(), "%02d", totalSeconds / 3600)
    val minutes = String.format(Locale.getDefault(), "%02d", (totalSeconds % 3600) / 60)
    val seconds = String.format(Locale.getDefault(), "%02d", (totalSeconds % 60))

    return "$hours:$minutes:$seconds"
}

fun Double.toFormattedKilometers(): String {
    return "${this.roundToDecimals(1)} km"
}

fun Duration.toFormattedPace(distanceKilometers: Double): String {
    if (this == Duration.ZERO || distanceKilometers <= 0.0) {
        return "-"
    }

    val secondsPerKm = (this.inWholeSeconds / distanceKilometers).roundToInt()
    val avgPaceMinutes = secondsPerKm / 60
    val avgPaceSeconds = String.format(Locale.getDefault(), "%02d", secondsPerKm % 60)

    return "$avgPaceMinutes:$avgPaceSeconds / km"
}

fun Double.toFormattedKmh(): String {
    return "${roundToDecimals(1)} km/h"
}

fun Int.toFormattedMeters(): String {
    return "$this m"
}

private fun Double.roundToDecimals(decimalCount: Int): Double {
    val factor = 10f.pow(decimalCount)
    return round(this * factor) / factor
}