package com.asifddlks.starwarscharacterexplorer.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.asifddlks.starwarscharacterexplorer.api.SWApiService
import com.asifddlks.starwarscharacterexplorer.db.CharacterDao
import com.asifddlks.starwarscharacterexplorer.repository.CharacterRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object CharacterRepositoryModule {
    @Provides
    fun provideCharacterRepository(
        characterDao: CharacterDao,
        service: SWApiService,
        dataStore: DataStore<Preferences>,
    ): CharacterRepository {
        return CharacterRepository(characterDao, service, dataStore)
    }

}