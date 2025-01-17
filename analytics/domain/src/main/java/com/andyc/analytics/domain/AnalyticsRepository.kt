package com.andyc.analytics.domain

interface AnalyticsRepository {
    suspend fun getAnalyticsValue(): AnalyticsValues
}