package com.andyc.run.presentation.run_overview.mapper

import com.andyc.core.domain.run.Run
import com.andyc.core.presentation.ui.formatted
import com.andyc.core.presentation.ui.toFormattedKilometers
import com.andyc.core.presentation.ui.toFormattedKmh
import com.andyc.core.presentation.ui.toFormattedMeters
import com.andyc.core.presentation.ui.toFormattedPace
import com.andyc.run.presentation.run_overview.model.RunUi
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Run.toRunUi(): RunUi {
    val localDateTime = dateTimeUtc
        .withZoneSameInstant(ZoneId.systemDefault())
    val formattedDateTime = DateTimeFormatter
        .ofPattern("MMM dd, yyyy - hh:mma")
        .format(localDateTime)
    
    val distanceKm = distanceMeters / 1000.0
    
    return RunUi(
        id = id!!,
        duration = duration.formatted(),
        dateTime = formattedDateTime,
        distance = distanceKm.toFormattedKilometers(),
        avgSpeed = avgSpeedKmh.toFormattedKmh(),
        maxSpeed = maxSpeedKmh.toFormattedKmh(),
        pace = duration.toFormattedPace(distanceKm),
        totalElevation = totalElevationMeters.toFormattedMeters(),
        mapPictureUrl = mapPictureUrl
    )
}