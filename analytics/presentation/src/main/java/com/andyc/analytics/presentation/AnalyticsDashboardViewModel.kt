package com.andyc.analytics.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andyc.analytics.domain.AnalyticsRepository
import kotlinx.coroutines.launch

class AnalyticsDashboardViewModel(
    private val analyticsRepository: AnalyticsRepository
): ViewModel() {
    var state by mutableStateOf(AnalyticsDashboardState())
        private set

    init {
        viewModelScope.launch {
            state = analyticsRepository.getAnalyticsValue().toAnalyticsDashboardState()
        }
    }
}