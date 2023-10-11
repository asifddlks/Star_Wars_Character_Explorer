package com.asifddlks.starwarscharacterexplorer.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.asifddlks.starwarscharacterexplorer.BuildConfig
import com.asifddlks.starwarscharacterexplorer.model.CharacterModel

@Database(
    entities = [CharacterModel::class],
    version = BuildConfig.SW_DB_VERSION,
)
abstract class SWDatabase : RoomDatabase() {
    abstract fun personDao(): CharacterDao
}