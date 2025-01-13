package com.andyc.run.presentation.active_run.maps

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.andyc.core.domain.location.LocationTimestamp
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Polyline

@Composable
fun RuniquePolylines(
    locations: List<List<LocationTimestamp>>
) {
    val polylines = remember(locations) {
        locations.map {
            it.zipWithNext { fromTimestamp, toTimestamp ->
                PolylineUi(
                    fromLocation = fromTimestamp.location.location,
                    toLocation = toTimestamp.location.location,
                    color = PolylineColorCalculator.locationsToColor(
                        fromLocation = fromTimestamp,
                        toLocation = toTimestamp
                    )
                )
            }
        }
    }

    polylines.forEach { polyline ->
        polyline.forEach { polylineUi ->
            Polyline(
                points = listOf(
                    LatLng(polylineUi.fromLocation.lat, polylineUi.fromLocation.long),
                    LatLng(polylineUi.toLocation.lat, polylineUi.toLocation.long)
                ),
                color = polylineUi.color,
                jointType = JointType.BEVEL
            )
        }
    }
}