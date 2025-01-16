package com.andyc.analytics.presentation

sealed interface AnalyticsAction {
    data object OnBackClick: AnalyticsAction
}