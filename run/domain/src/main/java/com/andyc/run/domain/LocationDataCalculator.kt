package com.andyc.run.domain

import com.andyc.core.domain.location.LocationTimestamp
import kotlin.math.roundToInt
import kotlin.time.DurationUnit

object LocationDataCalculator {
    fun getTotalDistanceMeters(locations: List<List<LocationTimestamp>>): Int {
        return locations
            .sumOf { timestampsPerLine ->
                timestampsPerLine.zipWithNext { location1, location2 ->
                    location1.location.location.distanceTo(location2.location.location)
                }.sum().roundToInt()
            }
    }

    fun getMaxSpeedKmh(locations: List<List<LocationTimestamp>>): Double {
        return locations.maxOf { locationSet ->
            locationSet.zipWithNext { fromLocation, toLocation ->
                val distance = fromLocation.location.location.distanceTo(toLocation.location.location)
                val hoursDifference = (toLocation.durationTimestamp - fromLocation.durationTimestamp)
                    .toDouble(DurationUnit.HOURS)

                if (hoursDifference == 0.0) {
                    0.0
                } else {
                    (distance / 1000.0) / hoursDifference
                }
            }.maxOrNull() ?: 0.0
        }
    }

    fun getTotalElevationMeters(locations: List<List<LocationTimestamp>>): Int {
        return locations.sumOf { locationSet ->
            locationSet.zipWithNext { fromLocation, toLocation ->
                val fromAltitude = fromLocation.location.altitude
                val toAltitude = toLocation.location.altitude
                (toAltitude - fromAltitude).coerceAtLeast(0.0)
            }.sum().roundToInt()
        }
    }
}