package com.asifddlks.starwarscharacterexplorer.api

import com.asifddlks.starwarscharacterexplorer.api.response.CharacterListResponse
import com.asifddlks.starwarscharacterexplorer.api.response.CharacterResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SWApiService {
    @GET("people")
    suspend fun fetchCharacterList(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ): CharacterListResponse

    @GET("people/{uid}")
    suspend fun fetchCharacter(
        @Path("uid") uid: String,
    ): CharacterResponse
}
