package com.asifddlks.starwarscharacterexplorer.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.asifddlks.starwarscharacterexplorer.model.CharacterModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
    @Insert(onConflict = REPLACE)
    fun insertAll(people: List<CharacterModel>)

    @Insert(onConflict = REPLACE)
    fun insertCharacter(character: CharacterModel)

    @Query("SELECT * FROM CharacterModel ORDER BY CAST(uid as Int) ASC")
    fun getAllPaged(): PagingSource<Int, CharacterModel>

    @Query("SELECT * FROM CharacterModel")
    fun getPeople(): List<CharacterModel>

    @Query("SELECT * FROM CharacterModel LIMIT :limit")
    fun getPeople(limit: Int): List<CharacterModel>

    @Query("SELECT * FROM CharacterModel WHERE uid = :uid")
    fun getCharacter(uid: String): Flow<CharacterModel>

    @Query("DELETE FROM CharacterModel")
    fun deleteAll()
}
