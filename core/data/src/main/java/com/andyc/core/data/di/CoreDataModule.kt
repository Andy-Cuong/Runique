package com.andyc.core.data.di

import com.andyc.core.data.auth.EncryptedSessionStorage
import com.andyc.core.data.networking.HttpClientFactory
import com.andyc.core.data.run.OfflineFirstRunRepository
import com.andyc.core.domain.SessionStorage
import com.andyc.core.domain.run.RunRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    single {
        HttpClientFactory(get()).build()
    }
    singleOf(::EncryptedSessionStorage).bind<SessionStorage>()
    singleOf(::OfflineFirstRunRepository).bind<RunRepository>()
}