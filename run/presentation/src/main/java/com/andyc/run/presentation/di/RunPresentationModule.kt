package com.andyc.run.presentation.di

import com.andyc.run.domain.RunningTracker
import com.andyc.run.presentation.active_run.ActiveRunViewModel
import com.andyc.run.presentation.run_overview.RunOverviewViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val runPresentationModule = module {
    singleOf(::RunningTracker)

    viewModelOf(::RunOverviewViewModel)
    viewModelOf(::ActiveRunViewModel)
}