package com.asifddlks.starwarscharacterexplorer.repository

import android.text.format.DateUtils
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.asifddlks.starwarscharacterexplorer.api.SWApiService
import com.asifddlks.starwarscharacterexplorer.db.CharacterDao
import com.asifddlks.starwarscharacterexplorer.model.CharacterModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val characterDao: CharacterDao,
    private val service: SWApiService,
    private val dataStore: DataStore<Preferences>
) {
    @OptIn(ExperimentalPagingApi::class)
    fun people(pageSize: Int) = Pager(
        config = PagingConfig(pageSize),
        remoteMediator = CharacterRemoteMediator(characterDao, service, dataStore),
    ) {
        characterDao.getAllPaged()
    }.flow

    suspend fun character(uid: String): Flow<CharacterModel> {
        val expTime = characterDao.getCharacter(uid).first().propertiesExpirationTime
        if (expTime == null || System.currentTimeMillis() > expTime) {
            val response = service.fetchCharacter(uid)
            val character = CharacterModel(
                response.result.uid,
                System.currentTimeMillis() + DateUtils.DAY_IN_MILLIS,
                response.result.properties
            )
            characterDao.insertCharacter(character)
        }
        return characterDao.getCharacter(uid)
    }
}
