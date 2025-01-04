package com.andyc.core.data.di

import com.andyc.core.data.auth.EncryptedSessionStorage
import com.andyc.core.data.networking.HttpClientFactory
import com.andyc.core.domain.SessionStorage
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    single {
        HttpClientFactory().build()
    }
    singleOf(::EncryptedSessionStorage).bind<SessionStorage>()
}