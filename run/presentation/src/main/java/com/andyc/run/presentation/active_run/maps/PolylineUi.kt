package com.andyc.run.presentation.active_run.maps

import androidx.compose.ui.graphics.Color
import com.andyc.core.domain.location.Location

data class PolylineUi(
    val fromLocation: Location,
    val toLocation: Location,
    val color: Color
)
