package com.alexander.rickmorty.di

import com.alexander.rickmorty.database.AppDatabase
import org.koin.dsl.module

val daoModule = module {
    single { get<AppDatabase>().characterDao() }
    single { get<AppDatabase>().remoteKeyDao() }
}
