package com.andyc.analytics.data.di

import com.andyc.analytics.data.RoomAnalyticsRepository
import com.andyc.analytics.domain.AnalyticsRepository
import com.andyc.core.database.RunDatabase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val analyticsDataModule = module {
    singleOf(::RoomAnalyticsRepository).bind<AnalyticsRepository>()
    single { get<RunDatabase>().analyticsDao }
}