package com.alexander.rickmorty

import android.app.Application
import com.alexander.rickmorty.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(listOf(
                    daoModule,
                    databaseModule,
                    networkModule,
                    viewModelModule,
                    datasourceModule,
                    repositoryModule
            ))
        }
    }
}
