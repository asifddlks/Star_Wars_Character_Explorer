package com.asifddlks.starwarscharacterexplorer.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.asifddlks.starwarscharacterexplorer.utils.testCharacter
import com.asifddlks.starwarscharacterexplorer.utils.testPeople
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CharacterDaoTest {
    private lateinit var db: SWDatabase
    private lateinit var dao: CharacterDao

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, SWDatabase::class.java).build()
        dao = db.personDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertPeopleAndGetAll() = runBlocking {
        db.personDao().insertAll(testPeople)
        val people = dao.getPeople()
        assertThat(people.size, equalTo(testPeople.size))
    }

    @Test
    fun insertPersonAndGetById() = runBlocking {
        db.personDao().insertCharacter(testCharacter)
        assertThat(dao.getCharacter(testCharacter.uid).first(), equalTo(testCharacter))
    }

}