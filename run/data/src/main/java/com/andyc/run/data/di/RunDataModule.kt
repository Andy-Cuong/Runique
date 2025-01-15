package com.andyc.run.data.di

import com.andyc.core.domain.run.SyncRunScheduler
import com.andyc.run.data.CreateRunWorker
import com.andyc.run.data.DeleteRunWorker
import com.andyc.run.data.FetchRunsWorker
import com.andyc.run.data.SyncRunWorkerScheduler
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val runDataModule = module {
    workerOf(::CreateRunWorker)
    workerOf(::FetchRunsWorker)
    workerOf(::DeleteRunWorker)

    singleOf(::SyncRunWorkerScheduler).bind<SyncRunScheduler>()
}