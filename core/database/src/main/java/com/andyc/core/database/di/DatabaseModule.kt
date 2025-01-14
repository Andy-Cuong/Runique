package com.andyc.core.database.di

import androidx.room.Room
import com.andyc.core.database.RoomLocalRunDatasource
import com.andyc.core.database.RunDatabase
import com.andyc.core.domain.run.LocalRunDataSource
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            RunDatabase::class.java,
            "run.db"
        ).build()
    }
    single { get<RunDatabase>().runDao }

    singleOf(::RoomLocalRunDatasource).bind<LocalRunDataSource>()
}