package com.alexander.rickmorty.di

import androidx.room.Room
import com.alexander.rickmorty.database.AppDatabase
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
                get(),
                AppDatabase::class.java,
                "rick&morty_database"
        ).fallbackToDestructiveMigration()
                .build()
    }
}
