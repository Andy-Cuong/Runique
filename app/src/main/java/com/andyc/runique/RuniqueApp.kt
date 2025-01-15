package com.andyc.runique

import android.app.Application
import com.andyc.auth.data.di.authDataModule
import com.andyc.auth.presentation.di.authViewModelModule
import com.andyc.core.data.di.coreDataModule
import com.andyc.core.database.di.databaseModule
import com.andyc.run.data.di.runDataModule
import com.andyc.run.location.di.locationModule
import com.andyc.run.network.di.runNetworkModule
import com.andyc.run.presentation.di.runPresentationModule
import com.andyc.runique.di.appModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import timber.log.Timber

class RuniqueApp : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@RuniqueApp)
            workManagerFactory()
            modules(
                appModule,
                coreDataModule,
                authDataModule,
                authViewModelModule,
                runPresentationModule,
                locationModule,
                databaseModule,
                runNetworkModule,
                runDataModule
            )
        }
    }
}