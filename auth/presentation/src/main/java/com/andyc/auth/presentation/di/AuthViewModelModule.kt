package com.andyc.auth.presentation.di

import com.andyc.auth.presentation.register.RegisterViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authViewModelModule = module {
    viewModelOf(::RegisterViewModel)
}