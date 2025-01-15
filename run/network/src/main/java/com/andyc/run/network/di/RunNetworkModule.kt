package com.andyc.run.network.di

import com.andyc.core.domain.run.RemoteRunDataSource
import com.andyc.run.network.KtorRemoteRunDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val runNetworkModule = module {
    singleOf(::KtorRemoteRunDataSource).bind<RemoteRunDataSource>()
}