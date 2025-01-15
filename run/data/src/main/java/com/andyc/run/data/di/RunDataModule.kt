package com.andyc.run.data.di

import com.andyc.run.data.CreateRunWorker
import com.andyc.run.data.DeleteRunWorker
import com.andyc.run.data.FetchRunsWorker
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.dsl.module

val runDataModule = module {
    workerOf(::CreateRunWorker)
    workerOf(::FetchRunsWorker)
    workerOf(::DeleteRunWorker)
}