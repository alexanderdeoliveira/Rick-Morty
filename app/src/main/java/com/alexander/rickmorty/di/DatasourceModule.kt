package com.alexander.rickmorty.di

import com.alexander.rickmorty.datasource.CharacterRemoteMediator
import org.koin.dsl.module

val datasourceModule = module {
    single { CharacterRemoteMediator(get(), get(), get(), get()) }
}
