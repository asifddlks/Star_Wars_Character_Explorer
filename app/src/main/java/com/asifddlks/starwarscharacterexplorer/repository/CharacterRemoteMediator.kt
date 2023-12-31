package com.asifddlks.starwarscharacterexplorer.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.paging.*
import androidx.paging.LoadType.*
import com.asifddlks.starwarscharacterexplorer.api.SWApiService
import com.asifddlks.starwarscharacterexplorer.db.CharacterDao
import com.asifddlks.starwarscharacterexplorer.model.CharacterModel
import kotlinx.coroutines.flow.first
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator(
    private val characterDao: CharacterDao,
    private val service: SWApiService,
    private val dataStore: DataStore<Preferences>,
) : RemoteMediator<Int, CharacterModel>() {

    private object PreferencesKeys {
        val PEOPLE_NEXT_PAGE_URL_KEY = stringPreferencesKey("people_next_page_url_prefs_key")
    }

    override suspend fun initialize(): InitializeAction {
        if (characterDao.getPeople(limit = 1).isEmpty()) {
            return InitializeAction.LAUNCH_INITIAL_REFRESH
        }
        return InitializeAction.SKIP_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterModel>
    ): MediatorResult {
        try {
            val page = when (loadType) {
                REFRESH -> 1
                PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                APPEND -> {
                    // If we receive `null` for APPEND, that means we have reached the end of
                    // pagination and there are no more items to load.
                    val nextPageUrl =
                        dataStore.data.first()[PreferencesKeys.PEOPLE_NEXT_PAGE_URL_KEY]
                            ?: return MediatorResult.Success(endOfPaginationReached = true)

                    val nextPage = nextPageUrl.toHttpUrlOrNull()
                        ?.queryParameter("page")
                        ?.toInt()
                        ?: return MediatorResult.Error(IOException("Unable to get next page"))

                    nextPage
                }
            }

            val data = service.fetchCharacterList(
                page = page,
                limit = state.config.pageSize,
            )

            if (loadType == REFRESH) {
                characterDao.deleteAll()
                dataStore.edit { prefs ->
                    prefs.remove(PreferencesKeys.PEOPLE_NEXT_PAGE_URL_KEY)
                }
            }

            dataStore.edit { prefs ->
                prefs[PreferencesKeys.PEOPLE_NEXT_PAGE_URL_KEY] = data.next.toString()
            }

            characterDao.insertAll(data.results)

            return MediatorResult.Success(endOfPaginationReached = page == data.totalPages)
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }
}