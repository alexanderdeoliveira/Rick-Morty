package com.alexander.rickmorty.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alexander.rickmorty.model.db.RemoteKey
import com.alexander.rickmorty.model.db.Character

@Database(entities = [Character::class, RemoteKey::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
    abstract fun remoteKeyDao(): RemoteKeyDao
}
