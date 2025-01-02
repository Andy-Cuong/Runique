package com.andyc.auth.data.di

import com.andyc.auth.data.EmailPatternValidator
import com.andyc.auth.domain.PatternValidator
import com.andyc.auth.domain.UserDataValidator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val authDataModule = module {
    single<PatternValidator> {
        EmailPatternValidator
    }
    singleOf(::UserDataValidator)
}