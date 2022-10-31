package com.alexander.rickmorty.di

import com.alexander.rickmorty.repository.CharacterRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { CharacterRepository(get(), get())}
}
