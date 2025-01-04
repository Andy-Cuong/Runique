package com.andyc.core.data.di

import com.andyc.core.data.networking.HttpClientFactory
import org.koin.dsl.module

val coreDataModule = module {
    single {
        HttpClientFactory().build()
    }
}